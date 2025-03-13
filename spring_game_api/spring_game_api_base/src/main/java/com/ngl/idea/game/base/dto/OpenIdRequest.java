package com.ngl.idea.game.base.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 获取OpenId请求DTO
 */
@Data
public class OpenIdRequest {
    /**
     * 用户来源
     */
    @NotBlank(message = "用户来源不能为空")
    private String userSource;

    /**
     * 授权码
     */
    @NotBlank(message = "授权码不能为空")
    private String code;
} 