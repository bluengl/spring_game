package com.ngl.idea.game.base.controller;

import com.ngl.idea.game.base.dto.*;
import com.ngl.idea.game.base.service.GmUserService;
import com.ngl.idea.game.common.config.manager.ConfigManager;
import com.ngl.idea.game.common.config.properties.SecurityProperties;
import com.ngl.idea.game.common.core.model.response.ApiResponse;
import com.ngl.idea.game.common.core.util.RsaUtils;
import com.ngl.idea.game.common.core.util.Sm2Utils;
import com.ngl.idea.game.common.security.filter.TokenUserHttpServletRequest;
import com.ngl.idea.game.base.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 基础API控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/base")
@RequiredArgsConstructor
public class BaseController {

    private final GmUserService gmUserService;

    private final ConfigManager configManager;

    /**
     * 获取OpenId
     *
     * @param request 获取OpenId请求
     * @return OpenId响应
     */
    @PostMapping("/getOpenId")
    public ApiResponse<OpenIdResponse> getOpenId(@RequestBody @Valid OpenIdRequest request) {
        log.info("获取OpenId请求: {}", request);
        OpenIdResponse response = gmUserService.getOpenId(request);
        return ApiResponse.success(response);
    }

    /**
     * 用户登录
     *
     * @param request 登录请求
     * @return 登录响应
     * @throws Exception
     */
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody @Valid LoginRequest request) throws Exception {
        log.info("用户登录请求: {}", request.getUsername());
        LoginResponse response = gmUserService.login(request);
        return ApiResponse.success(response);
    }

    /**
     * 用户注册
     *
     * @param request 注册请求
     * @return 用户DTO
     */
    @PostMapping("/register")
    public ApiResponse<UserDTO> register(@RequestBody @Valid RegisterRequest request) {
        log.info("用户注册请求: {}", request.getUsername());
        UserDTO userDTO = gmUserService.register(request);
        return ApiResponse.success(userDTO);
    }

    /**
     * 用户退出登录
     *
     * @param request HTTP请求
     * @return 成功响应
     */
    @PostMapping("/logout")
    public ApiResponse<Void> logout(HttpServletRequest request) {
        log.info("用户退出登录");
        // 这里可以添加令牌失效的逻辑，如果有需要的话
        return ApiResponse.success();
    }

    /**
     * 刷新token
     *
     * @param request 刷新token请求
     * @return 刷新token响应
     */
    @PostMapping("/refreshToken")
    public ApiResponse<LoginResponse> refreshToken(HttpServletRequest request) {
        log.info("刷新token请求: {}", request);
        TokenUserHttpServletRequest tokenUserHttpServletRequest = (TokenUserHttpServletRequest) request;
        String userId = tokenUserHttpServletRequest.getTokenUser().getUserId();
        LoginResponse response = gmUserService.refreshToken(userId);
        return ApiResponse.success(response);
    }

    /**
     * 获取系统配置信息
     *
     * @return 系统配置信息
     * @throws Exception
     */
    @PostMapping("/getSystemConfig")
    public ApiResponse<SystemConfig> getSystemConfig() throws Exception {
        SystemConfig systemConfig = new SystemConfig();
        systemConfig.setEnableEncryption(configManager.getEncryptionConfig().getEnabled());
        systemConfig.setEncryptionIgnoreUrls(configManager.getEncryptionConfig().getIgnore().getUrls());
        systemConfig.setSecurityIgnoreUrls(configManager.getSecurityConfig().getIgnore().getUrls());
        SecurityProperties.JwtProperties jwt = configManager.getSecurityConfig().getJwt();
        systemConfig.setJwtInfo(new SystemConfig.JwtInfo(jwt.getHeader(), jwt.getPrefix(), jwt.getAllowRefresh()));
        String encryptionType = configManager.getEncryptionConfig().getEncryptionType();
        systemConfig.setEncryptionType(encryptionType);
        if ("rsa".equals(encryptionType)) {
            try {
                // 获取Rsa公钥
                systemConfig.setRsaPublicKey(RsaUtils.publicKeyToBase64(RsaUtils.getRsaPublicKey()));
            } catch (Exception e) {
                log.error("获取Rsa公钥失败", e);
            }
        } else if ("sm2".equals(encryptionType)) {
            // 获取Sm2公钥
            try {
                systemConfig.setSm2PublicKey(Sm2Utils.publicKeyToBase64(Sm2Utils.getSm2PublicKey()));
            } catch (Exception e) {
                log.error("获取Sm2公钥失败", e);
            }
        }
        return ApiResponse.success(systemConfig);
    }
}
