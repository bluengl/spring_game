package com.ngl.idea.game.base.dto;

import lombok.Data;

/**
 * 微信code2session接口响应
 */
@Data
public class WechatSessionResponse {
    
    /**
     * 会话密钥
     */
    private String session_key;
    
    /**
     * 用户唯一标识
     */
    private String openid;
    
    /**
     * 用户在开放平台的唯一标识符
     */
    private String unionid;
    
    /**
     * 错误码
     */
    private Integer errcode;
    
    /**
     * 错误信息
     */
    private String errmsg;
} 