package com.ngl.idea.game.common.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 微信小程序配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "wechat.miniapp")
public class WechatProperties {
    
    /**
     * 微信小程序appid
     */
    private String appid;
    
    /**
     * 微信小程序secret
     */
    private String secret;
    
    /**
     * code换取session_key的URL
     */
    private String codeToSessionUrl = "https://api.weixin.qq.com/sns/jscode2session";
} 