package com.ngl.idea.game.common.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RefreshScope
@ConfigurationProperties(prefix = "game.encryption")
public class EncryptionProperties {
    private EncryptionConfig encryption = new EncryptionConfig();
    private Boolean enabled;
    private String encryptionType;
    private IgnoreConfig ignore = new IgnoreConfig();

    public static class EncryptionConfig {
        private String secret;
        private String iv;

        public String getSecret() {
            return secret;
        }

        public void setSecret(String secret) {
            this.secret = secret;
        }

        public String getIv() {
            return iv;
        }

        public void setIv(String iv) {
            this.iv = iv;
        }
    }

    public static class IgnoreConfig {
        private List<String> urls;

        public List<String> getUrls() {
            return urls;
        }

        public void setUrls(List<String> urls) {
            this.urls = urls;
        }
    }

    public EncryptionConfig getEncryption() {
        return encryption;
    }

    public void setEncryption(EncryptionConfig encryption) {
        this.encryption = encryption;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public IgnoreConfig getIgnore() {
        return ignore;
    }

    public void setIgnore(IgnoreConfig ignore) {
        this.ignore = ignore;
    }


    public String getEncryptionType() {
        return encryptionType;
    }

    public void setEncryptionType(String encryptionType) {
        this.encryptionType = encryptionType;
    }
}
