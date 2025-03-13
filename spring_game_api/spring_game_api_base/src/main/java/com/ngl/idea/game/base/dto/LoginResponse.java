package com.ngl.idea.game.base.dto;

import lombok.Data;

/**
 * 登录响应DTO
 */
@Data
public class LoginResponse {
    /**
     * 用户信息
     */
    private UserDTO user;

    /**
     * 访问令牌
     */
    private String accessToken;

    /**
     * 刷新令牌
     */
    private String refreshToken;

    /**
     * 令牌类型
     */
    private String tokenType = "Bearer";

    /**
     * token过期时间
     */
    private Long expiresIn;
} 