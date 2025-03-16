package com.ngl.idea.game.common.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RefreshScope
@ConfigurationProperties(prefix = "game.security")
public class SecurityProperties {
    private JwtProperties jwt = new JwtProperties();
    private IgnoreProperties ignore = new IgnoreProperties();
    private CorsProperties cors = new CorsProperties();

    public static class CorsProperties {
        private List<String> allowedOrigins;
        private List<String> exposedHeaders;
        private List<String> allowedHeaders;
        private List<String> allowedMethods;
        private Boolean allowCredentials;
        private Long maxAge;


        public List<String> getExposedHeaders() {
            return exposedHeaders;
        }

        public void setExposedHeaders(List<String> exposedHeaders) {
            this.exposedHeaders = exposedHeaders;
        }
        
        public List<String> getAllowedOrigins() {
            return allowedOrigins;
        }

        public void setAllowedOrigins(List<String> allowedOrigins) {
            this.allowedOrigins = allowedOrigins;
        }

        public List<String> getAllowedHeaders() {
            return allowedHeaders;
        }

        public void setAllowedHeaders(List<String> allowedHeaders) {
            this.allowedHeaders = allowedHeaders;
        }

        public List<String> getAllowedMethods() {
            return allowedMethods;
        }

        public void setAllowedMethods(List<String> allowedMethods) {
            this.allowedMethods = allowedMethods;
        }

        public Boolean getAllowCredentials() {
            return allowCredentials;
        }

        public void setAllowCredentials(Boolean allowCredentials) {
            this.allowCredentials = allowCredentials;
        }   

        public Long getMaxAge() {
            return maxAge;
        }

        public void setMaxAge(Long maxAge) {
            this.maxAge = maxAge;
        }
    }

    public static class JwtProperties {
        private String secret;
        private Long expiration;
        private String header;
        private String prefix;
        private Boolean allowRefresh;
        private Long refreshExpiration;
        private Boolean onceLogin;

        // Getters and Setters
        public String getSecret() {
            return secret;
        }

        public void setSecret(String secret) {
            this.secret = secret;
        }

        public Long getExpiration() {
            return expiration;
        }

        public void setExpiration(Long expiration) {
            this.expiration = expiration;
        }

        public String getHeader() {
            return header;
        }

        public void setHeader(String header) {
            this.header = header;
        }

        public String getPrefix() {
            return prefix;
        }

        public void setPrefix(String prefix) {
            this.prefix = prefix;
        }

        public Boolean getAllowRefresh() {
            return allowRefresh;
        }

        public void setAllowRefresh(Boolean allowRefresh) {
            this.allowRefresh = allowRefresh;
        }

        public Long getRefreshExpiration() {
            return refreshExpiration;
        }

        public void setRefreshExpiration(Long refreshExpiration) {
            this.refreshExpiration = refreshExpiration;
        }

        public Boolean getOnceLogin() {
            return onceLogin;
        }

        public void setOnceLogin(Boolean onceLogin) {
            this.onceLogin = onceLogin;
        }
    }

    public static class IgnoreProperties {
        private List<String> urls;

        public List<String> getUrls() {
            return urls;
        }

        public void setUrls(List<String> urls) {
            this.urls = urls;
        }
    }

    // Getters and Setters

    public CorsProperties getCors() {
        return cors;
    }

    public void setCors(CorsProperties cors) {
        this.cors = cors;
    }
    
    public JwtProperties getJwt() {
        return jwt;
    }

    public void setJwt(JwtProperties jwt) {
        this.jwt = jwt;
    }

    public IgnoreProperties getIgnore() {
        return ignore;
    }

    public void setIgnore(IgnoreProperties ignore) {
        this.ignore = ignore;
    }
}
