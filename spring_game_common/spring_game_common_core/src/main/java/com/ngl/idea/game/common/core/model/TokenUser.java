package com.ngl.idea.game.common.core.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class TokenUser implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户来源
     */
    private String userSource;

    /**
     * 密码
     */
    private String password;

}
