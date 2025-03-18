package com.ngl.idea.game.base.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 获取OpenId响应DTO
 */
@Data
@Schema(description = "获取OpenId响应信息")
public class OpenIdResponse {
    /**
     * OpenID
     */
    @Schema(description = "OpenID")
    private String openId;

    /**
     * 用户来源
     */
    @Schema(description = "用户来源")
    private String userSource;
} 