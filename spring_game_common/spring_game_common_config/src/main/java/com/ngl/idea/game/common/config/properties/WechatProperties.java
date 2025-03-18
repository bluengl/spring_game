package com.ngl.idea.game.common.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
@RefreshScope
@ConfigurationProperties(prefix = "game.wechat")
public class WechatProperties {
    
    private String appid;
    
    private String secret;
    
    private String codeToSessionUrl = "https://api.weixin.qq.com/sns/jscode2session";

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getCodeToSessionUrl() {
        return codeToSessionUrl;
    }

    public void setCodeToSessionUrl(String codeToSessionUrl) {
        this.codeToSessionUrl = codeToSessionUrl;
    }
} 