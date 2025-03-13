package com.ngl.idea.game.common.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RefreshScope
@ConfigurationProperties(prefix = "game.common")
public class CommonProperties {
    private String uploadPath;
    private List<String> allowedOrigins;
    private Long maxFileSize;
    private Long maxRequestSize;

    // Getters and Setters
    public String getUploadPath() {
        return uploadPath;
    }

    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }

    public List<String> getAllowedOrigins() {
        return allowedOrigins;
    }

    public void setAllowedOrigins(List<String> allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }

    public Long getMaxFileSize() {
        return maxFileSize;
    }

    public void setMaxFileSize(Long maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    public Long getMaxRequestSize() {
        return maxRequestSize;
    }

    public void setMaxRequestSize(Long maxRequestSize) {
        this.maxRequestSize = maxRequestSize;
    }
}
