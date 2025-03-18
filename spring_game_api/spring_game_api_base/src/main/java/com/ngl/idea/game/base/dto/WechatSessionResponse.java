package com.ngl.idea.game.base.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 微信code2session接口响应
 */
@Data
@Schema(description = "微信code2session接口响应")
public class WechatSessionResponse {
    
    /**
     * 会话密钥
     */
    @Schema(description = "会话密钥")
    private String session_key;
    
    /**
     * 用户唯一标识
     */
    @Schema(description = "用户唯一标识")
    private String openid;
    
    /**
     * 用户在开放平台的唯一标识符
     */
    @Schema(description = "用户在开放平台的唯一标识符")
    private String unionid;
    
    /**
     * 错误码
     */
    @Schema(description = "错误码")
    private Integer errcode;
    
    /**
     * 错误信息
     */
    @Schema(description = "错误信息")
    private String errmsg;
} 