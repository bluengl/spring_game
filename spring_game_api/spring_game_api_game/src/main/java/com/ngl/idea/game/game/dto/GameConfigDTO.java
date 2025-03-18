package com.ngl.idea.game.game.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 游戏配置信息DTO
 */
@Data
@Schema(description = "游戏配置信息")
public class GameConfigDTO {
    
    @Schema(description = "配置ID")
    private String configId;
    
    @Schema(description = "游戏ID")
    private String gameId;
    
    @Schema(description = "配置键")
    private String configKey;
    
    @Schema(description = "配置值")
    private String configValue;
    
    @Schema(description = "配置描述")
    private String configDescription;
    
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
} 