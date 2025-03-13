package com.ngl.idea.game.base.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SystemConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 是否启用加密
     */
    private Boolean enableEncryption;

    /**
     * 加密忽略URL
     */
    private List<String> encryptionIgnoreUrls;

    /**
     * 安全忽略URL
     */
    private List<String> securityIgnoreUrls;

    /**
     * JWT信息
     */
    private JwtInfo jwtInfo;

    /**
     * 密钥信息
     */
    private KeyInfo keyInfo;

    /**
     * 加密类型
     */
    private String encryptionType;

    /**
     * rsa公钥
     */
    private String rsaPublicKey;

    /**
     * sm2公钥
     */
    private String sm2PublicKey;

    public static class KeyInfo {
        /**
         * RSA公钥
         */
        private String rsaPublicKey;

        /**
         * SM2公钥
         */
        private String sm2PublicKey;

        public String getRsaPublicKey() {   
            return rsaPublicKey;
        }

        public void setRsaPublicKey(String rsaPublicKey) {
            this.rsaPublicKey = rsaPublicKey;
        }
        
        public String getSm2PublicKey() {
            return sm2PublicKey;
        }

        public void setSm2PublicKey(String sm2PublicKey) {
            this.sm2PublicKey = sm2PublicKey;
        }   

    }

    public static class JwtInfo {

        public JwtInfo() {

        }

        public JwtInfo(String header, String prefix, Boolean allowRefresh) {
            this.header = header;
            this.prefix = prefix;
            this.allowRefresh = allowRefresh;
        }

        private String header;
        private String prefix;
        private Boolean allowRefresh;

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
    }

}
