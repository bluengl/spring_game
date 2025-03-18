package com.ngl.idea.game.game.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 游戏配置信息DTO
 */
@Data
@ApiModel(description = "游戏配置信息")
public class GameConfigDTO {
    
    @ApiModelProperty("配置ID")
    private String configId;
    
    @ApiModelProperty("游戏ID")
    private String gameId;
    
    @ApiModelProperty("配置键")
    private String configKey;
    
    @ApiModelProperty("配置值")
    private String configValue;
    
    @ApiModelProperty("配置描述")
    private String configDescription;
    
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
} 