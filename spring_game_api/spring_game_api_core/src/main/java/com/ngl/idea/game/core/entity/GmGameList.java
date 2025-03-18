package com.ngl.idea.game.core.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("GM_GAME_LIST")
public class GmGameList {
    /**
     * 游戏ID
     */
    @TableId(value = "GAME_ID", type = IdType.ASSIGN_UUID)
    private String gameId;  
    /**
     * 游戏名称
     */
    @TableField("GAME_NAME")
    private String gameName;

    /**
     * 游戏编码 
     */
    @TableField("GAME_CODE")
    private String gameCode;

    /**
     * 游戏类型
     */ 
    @TableField("GAME_TYPE")
    private String gameType;
    /**
     * 游戏图标
     */
    @TableField("GAME_ICON")
    private String gameIcon;
    /**
     * 游戏背景
     */
    @TableField("GAME_BG")
    private String gameBg;
    /**
     * 游戏描述
     */
    @TableField("GAME_DESCRIPTION")
    private String gameDescription;

    /**
     * 排序
     */
    @TableField("ORDER_NUM")
    private String orderNum;

    /**
     * 是否新游戏
     */
    @TableField("IS_NEW")
    private String isNew;
    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
    /**
     * 是否销毁
     */
    @TableField("DESTROY")
    private String destroy;
    /**
     * 是否有效
     */
    @TableField("EFFECTIVE")
    private String effective;
}