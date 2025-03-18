package com.ngl.idea.game.base.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户DTO
 */
@Data
@Schema(description = "用户信息")
public class UserDTO {
    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    private String userId;

    /**
     * 用户名
     */
    @Schema(description = "用户名")
    private String username;

    /**
     * 用户来源
     */
    @Schema(description = "用户来源")
    private String userSource;

    /**
     * OpenID
     */
    @Schema(description = "OpenID")
    private String openId;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
} 