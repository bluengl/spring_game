package com.ngl.idea.game.game.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户游戏分数DTO
 */
@Data
@Schema(description = "用户游戏分数信息")
public class GmHigeScoreDTO {
    
    @Schema(description = "用户ID")
    private String userId;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "游戏ID")
    private String gameId;

    @Schema(description = "游戏等级")
    private Integer gameLevel;
    
    @Schema(description = "用户最高分数")
    private Integer gameScore;
    
    @Schema(description = "创建时间")
    private String createTime;
    
    @Schema(description = "更新时间")
    private String updateTime;

    @Schema(description = "排名")
    private Integer rank;
} 