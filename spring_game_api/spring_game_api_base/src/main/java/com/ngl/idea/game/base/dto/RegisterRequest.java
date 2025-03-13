package com.ngl.idea.game.base.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 注册请求DTO
 */
@Data
public class RegisterRequest {
    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    private String username;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;

    /**
     * 用户来源
     */
    private String userSource;

    /**
     * OpenID
     */
    private String openId;
} 