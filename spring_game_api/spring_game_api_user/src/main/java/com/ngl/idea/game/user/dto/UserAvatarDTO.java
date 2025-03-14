package com.ngl.idea.game.user.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户头像数据传输对象
 */
@Data
public class UserAvatarDTO {
    /**
     * 头像ID
     */
    private String avatarId;
    
    /**
     * 用户ID
     */
    private String userId;
    
    /**
     * Base64编码的头像数据
     */
    private String base64;
    
    /**
     * 版本号
     */
    private String version;
    
    /**
     * 状态
     */
    private String status;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
} 