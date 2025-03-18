package com.ngl.idea.game.game.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 游戏记录批量保存条目DTO
 */
@Data
@Schema(description = "游戏记录批量保存条目")
public class GameRecordBatchDTO {
    
    @Schema(description = "游戏等级")
    private Integer gameLevel;
    
    @Schema(description = "游戏分数")
    private Integer gameScore;
} 