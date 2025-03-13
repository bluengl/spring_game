package com.ngl.idea.game.base.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户DTO
 */
@Data
public class UserDTO {
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
     * OpenID
     */
    private String openId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
} 