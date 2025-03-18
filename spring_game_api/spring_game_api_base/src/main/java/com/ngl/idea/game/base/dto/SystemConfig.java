package com.ngl.idea.game.base.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Schema(description = "系统配置信息")
public class SystemConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 是否启用加密
     */
    @Schema(description = "是否启用加密")
    private Boolean enableEncryption;

    /**
     * 加密忽略URL
     */
    @Schema(description = "加密忽略URL")
    private List<String> encryptionIgnoreUrls;

    /**
     * 安全忽略URL
     */
    @Schema(description = "安全忽略URL")
    private List<String> securityIgnoreUrls;

    /**
     * JWT信息
     */
    @Schema(description = "JWT信息")
    private JwtInfo jwtInfo;

    /**
     * 密钥信息
     */
    @Schema(description = "密钥信息")
    private KeyInfo keyInfo;

    /**
     * 加密类型
     */
    @Schema(description = "加密类型")
    private String encryptionType;

    /**
     * rsa公钥
     */
    @Schema(description = "RSA公钥")
    private String rsaPublicKey;

    /**
     * sm2公钥
     */
    @Schema(description = "SM2公钥")
    private String sm2PublicKey;

    @Schema(description = "密钥信息")
    public static class KeyInfo {
        /**
         * RSA公钥
         */
        @Schema(description = "RSA公钥")
        private String rsaPublicKey;

        /**
         * SM2公钥
         */
        @Schema(description = "SM2公钥")
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

    @Schema(description = "JWT信息")
    public static class JwtInfo {
        public JwtInfo() {
        }

        public JwtInfo(String header, String prefix, Boolean allowRefresh) {
            this.header = header;
            this.prefix = prefix;
            this.allowRefresh = allowRefresh;
        }

        @Schema(description = "JWT头部")
        private String header;

        @Schema(description = "JWT前缀")
        private String prefix;

        @Schema(description = "是否允许刷新")
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
