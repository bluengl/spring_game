package com.ngl.idea.game.base.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 微信登录请求
 */
@Data
@Schema(description = "微信登录请求")
public class WechatLoginRequest {

    /**
     * 用户信息
     */
    @Data
    @Schema(description = "微信用户信息")
    public static class UserInfo {
       /**
        * 昵称
        */
        @Schema(description = "昵称")
        private String nickName;

        /**
         * 性别
         */
        @Schema(description = "性别")
        private Integer gender; 

        /**
         * 语言
         */
        @Schema(description = "语言")
        private String language;

        /** 
         * 城市
         */
        @Schema(description = "城市")
        private String city;

        /**
         * 省份
         */
        @Schema(description = "省份")
        private String province;

        /**
         * 国家
         */
        @Schema(description = "国家")
        private String country;

        /**
         * 头像
         */
        @Schema(description = "头像URL")
        private String avatarUrl;

        /**
         * 是否降级
         */
        @Schema(description = "是否降级")
        private Boolean isDemote;
    }

    /**
     * 微信code
     */
    @Schema(description = "微信code")
    private String code;

    /**
     * 微信用户信息
     */
    @Schema(description = "微信用户信息")
    private UserInfo userInfo;
}
