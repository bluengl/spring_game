package com.ngl.idea.game.base.dto;

import lombok.Data;

/**
 * 获取OpenId响应DTO
 */
@Data
public class OpenIdResponse {
    /**
     * OpenID
     */
    private String openId;

    /**
     * 用户来源
     */
    private String userSource;
} 