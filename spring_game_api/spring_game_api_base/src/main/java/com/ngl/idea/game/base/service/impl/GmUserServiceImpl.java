package com.ngl.idea.game.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ngl.idea.game.base.dto.*;
import com.ngl.idea.game.base.service.GmUserService;
import com.ngl.idea.game.common.config.manager.ConfigManager;
import com.ngl.idea.game.common.config.properties.WechatProperties;
import com.ngl.idea.game.common.core.exception.BusinessException;
import com.ngl.idea.game.common.core.model.response.ResultCode;
import com.ngl.idea.game.common.core.util.AesUtils;
import com.ngl.idea.game.common.core.util.BcryptUtils;
import com.ngl.idea.game.common.core.util.HttpUtils;
import com.ngl.idea.game.common.security.util.JwtUtils;
import com.ngl.idea.game.core.entity.GmUser;
import com.ngl.idea.game.core.mapper.GmUserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
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
    @Transactional(rollbackFor = Exception.class)
    public LoginResponse wxLogin(WechatLoginRequest request) {
        log.info("微信用户登录请求: {}", request.getCode());
        
        if (!StringUtils.hasText(request.getCode())) {
            throw new BusinessException(ResultCode.PARAM_ERROR.getCode(), "微信code不能为空");
        }
        
        // 调用微信API获取openId
        WechatSessionResponse sessionResponse = getWechatSession(request.getCode());
        
        if (sessionResponse == null || !StringUtils.hasText(sessionResponse.getOpenid())) {
            throw new BusinessException(ResultCode.INTERNAL_SERVER_ERROR.getCode(), "获取微信openId失败");
        }
        
        String openId = sessionResponse.getOpenid();
        log.info("获取到微信openId: {}", openId);
        
        // 根据openId查询用户
        GmUser user = findByOpenId(openId, "WECHAT");
        
        // 如果用户不存在，则注册新用户
        if (user == null) {
            log.info("用户不存在，开始注册新用户: {}", openId);
            // 创建注册请求
            RegisterRequest registerRequest = new RegisterRequest();
            // 使用随机用户名
            String username = "wx_" + UUID.randomUUID().toString().substring(0, 8);
            registerRequest.setUsername(username);
            // 生成随机密码
            String password = UUID.randomUUID().toString().substring(0, 16);
            // 模拟加密密码
            registerRequest.setPassword(AesUtils.encryptSimpleJs(password, "12345678901234567890123456789012"));
            registerRequest.setOpenId(openId);
            registerRequest.setUserSource("WECHAT");
            
            // 如果有用户信息，设置昵称为用户名
            WechatLoginRequest.UserInfo userInfo = request.getUserInfo();
            if (userInfo != null && StringUtils.hasText(userInfo.getNickName())) {
                registerRequest.setUsername(userInfo.getNickName());
            }
            
            // 注册用户
            register(registerRequest);
            
            // 获取新注册的用户
            user = findByOpenId(openId, "WECHAT");
        }
        
        // 生成token
        String accessToken = jwtUtils.generateToken(user);
        String refreshToken = jwtUtils.generateRefreshToken(user);
        
        // 构建响应
        LoginResponse response = new LoginResponse();
        response.setUser(convertToDTO(user));
        response.setAccessToken(accessToken);
        if (configManager.getSecurityConfig().getJwt().getAllowRefresh()) {
            response.setRefreshToken(refreshToken);
        }
        
        log.info("微信用户登录成功: {}", openId);
        return response;
    }
    
    /**
     * 调用微信接口获取session信息
     *
     * @param code 微信授权code
     * @return 微信session响应
     */
    private WechatSessionResponse getWechatSession(String code) {
        try {
            WechatProperties wechatProperties = configManager.getWechatConfig();
            String url = wechatProperties.getCodeToSessionUrl();
            Map<String, String> params = new HashMap<>();
            params.put("appid", wechatProperties.getAppid());
            params.put("secret", wechatProperties.getSecret());
            params.put("js_code", code);
            params.put("grant_type", "authorization_code");
            
            log.info("请求微信code2session接口: {}, 参数: {}", url, params);
            return HttpUtils.doGet(url, params, WechatSessionResponse.class);
        } catch (Exception e) {
            log.error("请求微信code2session接口失败", e);
            return null;
        }
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
        log.debug("开始解密密码 [username={}]", request.getUsername());
        String encryptedPassword = request.getPassword();
        // 添加请求密码日志，方便问题排查
        log.debug("加密的密码格式: {}", encryptedPassword);
        // 尝试使用新的简单解密方法
        String decryptedPassword = AesUtils.decryptSimpleJs(encryptedPassword, "12345678901234567890123456789012");
        // 验证密码
        if (!BcryptUtils.matches(decryptedPassword, user.getPassword())) {
            log.warn("登录失败: 密码错误 [username={}]", request.getUsername());
            throw new BusinessException(ResultCode.USER_PASSWORD_ERROR);
        }
        log.debug("密码验证成功，生成令牌 [username={}]", request.getUsername());
        try {
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
            throw new BusinessException(ResultCode.TOKEN_GENERATE_ERROR);
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
            user.setEffective("1");
            user.setDestroy("0");
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
        // 构建响应
        LoginResponse response = new LoginResponse();
        response.setUser(convertToDTO(user));
        response.setAccessToken(accessToken);
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
        dto.setCreateTime(user.getCreateTime());
        return dto;
    }
}
