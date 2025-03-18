package com.ngl.idea.game.game.dto;

import lombok.Data;

/**
 * 游戏DTO
 */
@Data
public class GameDTO {
    /**
     * 游戏ID
     */
    private String gameId;
    
    /**
     * 游戏名称
     */
    private String gameName;

    /**
     * 游戏编码
     */
    private String gameCode;
    
    /**
     * 游戏类型
     */
    private String gameType;
    
    /**
     * 游戏图标
     */
    private String gameIcon;
    
    /**
     * 游戏背景
     */
    private String gameBg;
    
    /**
     * 游戏描述
     */
    private String gameDescription;
    
    /**
     * 排序
     */
    private Integer orderNum;
    
    /**
     * 是否新游戏(1/0)
     */
    private String isNew;
} 