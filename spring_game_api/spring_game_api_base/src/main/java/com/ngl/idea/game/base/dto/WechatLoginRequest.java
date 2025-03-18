package com.ngl.idea.game.base.dto;

import lombok.Data;

/**
 * 微信登录请求
 */
@Data
public class WechatLoginRequest {

    /**
     * 用户信息
     */
    @Data
    public static class UserInfo {
       /**
        * 昵称
        */
        private String nickName;

        /**
         * 性别
         */
        private Integer gender; 

        /**
         * 语言
         */
        private String language;

        /** 
         * 城市
         */
        private String city;

        /**
         * 省份
         */
        private String province;

        /**
         * 国家
         */
        private String country;

        /**
         * 头像
         */
        private String avatarUrl;

        /**
         * 是否降级
         */
        private Boolean isDemote;
    }

    /**
     * 微信code
     */
    private String code;

    /**
     * 微信用户信息
     */
    private UserInfo userInfo;
}
