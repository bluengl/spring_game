package com.ngl.idea.game.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ngl.idea.game.base.dto.*;
import com.ngl.idea.game.base.service.GmUserService;
import com.ngl.idea.game.common.config.manager.ConfigManager;
import com.ngl.idea.game.common.core.exception.BusinessException;
import com.ngl.idea.game.common.core.model.response.ResultCode;
import com.ngl.idea.game.common.core.util.AesUtils;
import com.ngl.idea.game.common.core.util.BcryptUtils;
import com.ngl.idea.game.common.core.util.RedisUtil;
import com.ngl.idea.game.common.core.util.UUIDUtils;
import com.ngl.idea.game.common.security.util.JwtUtils;
import com.ngl.idea.game.core.entity.GmUser;
import com.ngl.idea.game.core.mapper.GmUserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 用户服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GmUserServiceImpl implements GmUserService {

    private final GmUserMapper gmUserMapper;
    private final JwtUtils jwtUtils;
    private final ConfigManager configManager;
    
    @Override
    public OpenIdResponse getOpenId(OpenIdRequest request) {
        // 这里应该调用第三方平台API获取OpenId
        // 为了演示，我们生成一个模拟的OpenId
        String openId = UUID.randomUUID().toString().replace("-", "");

        OpenIdResponse response = new OpenIdResponse();
        response.setOpenId(openId);
        response.setUserSource(request.getUserSource());
        return response;
    }

    @Override
    public LoginResponse login(LoginRequest request) throws Exception {
        log.info("用户登录请求: {}", request.getUsername());

        // 根据用户名查询用户
        GmUser user = findByUsername(request.getUsername());
        if (user == null) {
            log.warn("登录失败: 用户不存在 [username={}]", request.getUsername());
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        try {
            log.debug("开始解密密码 [username={}]", request.getUsername());
            String encryptedPassword = request.getPassword();

            // 添加请求密码日志，方便问题排查
            log.debug("加密的密码格式: {}", encryptedPassword);

            // 尝试使用新的简单解密方法
            String decryptedPassword;
            try {
                // 首先尝试新的简单解密方法
                decryptedPassword = AesUtils.decryptSimpleJs(encryptedPassword, "12345678901234567890123456789012");
                log.debug("使用SimpleJs解密成功 [username={}]", request.getUsername());
            } catch (Exception e) {
                // 如果失败，尝试使用原有方法
                log.warn("SimpleJs解密失败，尝试使用原有方法: {}", e.getMessage());
                decryptedPassword = AesUtils.decryptFromJS(encryptedPassword, "12345678901234567890123456789012");
                log.debug("原有解密方法成功 [username={}]", request.getUsername());
            }
            // 验证密码
            if (!BcryptUtils.matches(decryptedPassword, user.getPassword())) {
                log.warn("登录失败: 密码错误 [username={}]", request.getUsername());
                throw new BusinessException(ResultCode.USER_PASSWORD_ERROR);
            }

            log.debug("密码验证成功，生成令牌 [username={}]", request.getUsername());
            
            // 生成令牌
            String accessToken = jwtUtils.generateToken(user);
            String refreshToken = jwtUtils.generateRefreshToken(user);
            // 构建响应
            LoginResponse response = new LoginResponse();
            response.setUser(convertToDTO(user));
            response.setAccessToken(accessToken);
            if (configManager.getSecurityConfig().getJwt().getAllowRefresh()) {
                response.setRefreshToken(refreshToken);
            }
            log.info("用户登录成功 [username={}]", request.getUsername());
            return response;
        } catch (Exception e) {
            log.error("用户登录失败 [username={}]: {}", request.getUsername(), e.getMessage(), e);
            // 判断异常类型，提供更精确的错误信息
            if (e.getMessage() != null && e.getMessage().contains("Given final block not properly padded")) {
                throw new BusinessException(ResultCode.DECRYPT_ERROR.getCode(), "密码解密失败：填充错误");
            } else if (e.getMessage() != null && e.getMessage().contains("Input byte array has incorrect ending byte")) {
                throw new BusinessException(ResultCode.DECRYPT_ERROR.getCode(), "密码解密失败：Base64格式错误");
            } else if (e instanceof BusinessException) {
                throw (BusinessException) e;
            } else {
                throw new BusinessException(ResultCode.USER_PASSWORD_ERROR);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserDTO register(RegisterRequest request) {
        try {
            // 检查用户名是否已存在
            GmUser existingUser = findByUsername(request.getUsername());
            if (existingUser != null) {
                throw new BusinessException(ResultCode.USER_ALREADY_EXIST);
            }

            // 如果提供了OpenId，检查是否已关联用户
            if (request.getOpenId() != null && !request.getOpenId().isEmpty()) {
                GmUser userByOpenId = findByOpenId(request.getOpenId(), request.getUserSource());
                if (userByOpenId != null) {
                    throw new BusinessException(ResultCode.OPEN_ID_ALREADY_BOUND);
                }
            }

            // 尝试解密密码
            String decryptedPassword;
            try {
                // 首先尝试新的简单解密方法
                decryptedPassword = AesUtils.decryptSimpleJs(request.getPassword(), "12345678901234567890123456789012");
                log.debug("使用SimpleJs解密成功，注册用户: [username={}]", request.getUsername());
            } catch (Exception e) {
                // 如果失败，尝试使用原有方法
                log.warn("SimpleJs解密失败，尝试使用原有方法: {}", e.getMessage());
                decryptedPassword = AesUtils.decryptFromJS(request.getPassword(), "12345678901234567890123456789012");
                log.debug("原有解密方法成功，注册用户: [username={}]", request.getUsername());
            }

            // 创建新用户
            GmUser user = new GmUser();
            user.setUsername(request.getUsername());
            // 加密密码后存储
            user.setPassword(BcryptUtils.encode(decryptedPassword));
            user.setUserSource(request.getUserSource());
            user.setOpenId(request.getOpenId());
            user.setEffective("Y");
            user.setDestroy("N");
            user.setCreateTime(LocalDateTime.now());
            user.setUpdateTime(LocalDateTime.now());

            // 保存用户
            gmUserMapper.insert(user);

            return convertToDTO(user);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("用户注册失败: {}", e.getMessage(), e);
            throw new BusinessException(ResultCode.INTERNAL_SERVER_ERROR.getCode(), "用户注册失败");
        }
    }

    @Override
    public LoginResponse refreshToken(String userId) {
        GmUser user = findByUserId(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        // 生成令牌
        String accessToken = jwtUtils.generateToken(user);
        String refreshToken = jwtUtils.generateRefreshToken(user);
        // 构建响应
        LoginResponse response = new LoginResponse();
        response.setUser(convertToDTO(user));
        response.setAccessToken(accessToken);
        if (configManager.getSecurityConfig().getJwt().getAllowRefresh()) {
            response.setRefreshToken(refreshToken);
        }
        return response;
    }

    @Override
    public GmUser findByUsername(String username) {
        LambdaQueryWrapper<GmUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GmUser::getUsername, username)
                .eq(GmUser::getEffective, "1")
                .eq(GmUser::getDestroy, "0");
        return gmUserMapper.selectOne(queryWrapper);
    }

    private GmUser findByUserId(String userId) {
        LambdaQueryWrapper<GmUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GmUser::getUserId, userId)
                   .eq(GmUser::getEffective, "1")
                   .eq(GmUser::getDestroy, "0");
        return gmUserMapper.selectOne(queryWrapper);
    }

    @Override
    public GmUser findByOpenId(String openId, String userSource) {
        LambdaQueryWrapper<GmUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GmUser::getOpenId, openId)
                   .eq(GmUser::getUserSource, userSource)
                   .eq(GmUser::getEffective, "1")
                   .eq(GmUser::getDestroy, "0");
        return gmUserMapper.selectOne(queryWrapper);
    }

    @Override
    public UserDTO convertToDTO(GmUser user) {
        if (user == null) {
            return null;
        }

        UserDTO dto = new UserDTO();
        dto.setUserId(user.getUserId());
        dto.setUsername(user.getUsername());
        dto.setUserSource(user.getUserSource());
        dto.setOpenId(user.getOpenId());
        dto.setCreateTime(user.getCreateTime());
        return dto;
    }
}
