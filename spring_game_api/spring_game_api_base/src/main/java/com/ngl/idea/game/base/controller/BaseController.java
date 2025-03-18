package com.ngl.idea.game.base.controller;

import com.ngl.idea.game.base.dto.*;
import com.ngl.idea.game.base.service.GmUserService;
import com.ngl.idea.game.common.config.manager.ConfigManager;
import com.ngl.idea.game.common.config.properties.SecurityProperties;
import com.ngl.idea.game.common.core.model.response.ApiResponse;
import com.ngl.idea.game.common.core.util.RsaUtils;
import com.ngl.idea.game.common.core.util.Sm2Utils;
import com.ngl.idea.game.core.controller.GameBaseController;
import com.ngl.idea.game.base.dto.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
@Tag(name = "基础服务API")
public class BaseController extends GameBaseController {

    private final GmUserService gmUserService;

    private final ConfigManager configManager;

    /**
     * 获取OpenId
     *
     * @param userSource 用户来源
     * @param code 授权码
     * @return OpenId响应
     */
    @Operation(summary = "获取OpenId")
    @PostMapping("/getOpenId")
    public ApiResponse<OpenIdResponse> getOpenId(
            @Parameter(description = "用户来源", required = true) @RequestParam String userSource,
            @Parameter(description = "授权码", required = true) @RequestParam String code) {
        OpenIdRequest request = new OpenIdRequest();
        request.setUserSource(userSource);
        request.setCode(code);
        log.info("获取OpenId请求: {}", request);
        OpenIdResponse response = gmUserService.getOpenId(request);
        return ApiResponse.success(response);
    }

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @param userSource 用户来源（可选）
     * @return 登录响应
     * @throws Exception
     */
    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(
            @Parameter(description = "用户名", required = true) @RequestParam String username,
            @Parameter(description = "密码", required = true) @RequestParam String password,
            @Parameter(description = "用户来源", required = false) @RequestParam(required = false) String userSource) throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername(username);
        request.setPassword(password);
        request.setUserSource(userSource);
        log.info("用户登录请求: {}", request.getUsername());
        LoginResponse response = gmUserService.login(request);
        return ApiResponse.success(response);
    }

    /**
     * 微信登录
     *
     * @param code 微信code
     * @param nickName 昵称
     * @param gender 性别
     * @param language 语言
     * @param city 城市
     * @param province 省份
     * @param country 国家
     * @param avatarUrl 头像URL
     * @return 登录响应
     */
    @Operation(summary = "微信登录")
    @PostMapping("/wxLogin")
    public ApiResponse<LoginResponse> wxLogin(
            @Parameter(description = "微信code", required = true) @RequestParam String code,
            @Parameter(description = "昵称", required = false) @RequestParam(required = false) String nickName,
            @Parameter(description = "性别", required = false) @RequestParam(required = false) Integer gender,
            @Parameter(description = "语言", required = false) @RequestParam(required = false) String language,
            @Parameter(description = "城市", required = false) @RequestParam(required = false) String city,
            @Parameter(description = "省份", required = false) @RequestParam(required = false) String province,
            @Parameter(description = "国家", required = false) @RequestParam(required = false) String country,
            @Parameter(description = "头像URL", required = false) @RequestParam(required = false) String avatarUrl) {
        
        WechatLoginRequest request = new WechatLoginRequest();
        request.setCode(code);
        
        WechatLoginRequest.UserInfo userInfo = new WechatLoginRequest.UserInfo();
        userInfo.setNickName(nickName);
        userInfo.setGender(gender);
        userInfo.setLanguage(language);
        userInfo.setCity(city);
        userInfo.setProvince(province);
        userInfo.setCountry(country);
        userInfo.setAvatarUrl(avatarUrl);
        
        request.setUserInfo(userInfo);
        
        log.info("微信登录请求");
        LoginResponse response = gmUserService.wxLogin(request);
        return ApiResponse.success(response);
    }

    /**
     * 用户注册
     *
     * @param username 用户名
     * @param password 密码
     * @param userSource 用户来源（可选）
     * @param openId OpenID（可选）
     * @return 用户DTO
     */
    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public ApiResponse<UserDTO> register(
            @Parameter(description = "用户名", required = true) @RequestParam String username,
            @Parameter(description = "密码", required = true) @RequestParam String password,
            @Parameter(description = "用户来源", required = false) @RequestParam(required = false) String userSource,
            @Parameter(description = "OpenID", required = false) @RequestParam(required = false) String openId) {
        
        RegisterRequest request = new RegisterRequest();
        request.setUsername(username);
        request.setPassword(password);
        request.setUserSource(userSource);
        request.setOpenId(openId);
        
        log.info("用户注册请求: {}", request.getUsername());
        UserDTO userDTO = gmUserService.register(request);
        return ApiResponse.success(userDTO);
    }

    /**
     * 用户退出登录
     *
     * @return 成功响应
     */
    @Operation(summary = "退出登录")
    @PostMapping("/logout")
    public ApiResponse<Void> logout() {
        log.info("用户退出登录");
        // 这里可以添加令牌失效的逻辑，如果有需要的话
        return ApiResponse.success();
    }

    /**
     * 刷新token
     *
     * @return 刷新token响应
     */
    @Operation(summary = "刷新Token")
    @PostMapping("/refreshToken")
    public ApiResponse<LoginResponse> refreshToken() {
        log.info("刷新token请求");
        String userId = getUserId();
        LoginResponse response = gmUserService.refreshToken(userId);
        return ApiResponse.success(response);
    }

    /**
     * 获取系统配置信息
     *
     * @return 系统配置信息
     * @throws Exception
     */
    @Operation(summary = "获取系统配置")
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
