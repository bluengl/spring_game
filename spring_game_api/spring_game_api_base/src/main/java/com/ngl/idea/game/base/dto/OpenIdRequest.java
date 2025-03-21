package com.ngl.idea.game.base.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 获取OpenId请求DTO
 */
@Data
@Schema(description = "获取OpenId请求")
public class OpenIdRequest {
    /**
     * 用户来源
     */
    @NotBlank(message = "用户来源不能为空")
    @Schema(description = "用户来源", required = true)
    private String userSource;

    /**
     * 授权码
     */
    @NotBlank(message = "授权码不能为空")
    @Schema(description = "授权码", required = true)
    private String code;
} 