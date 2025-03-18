package com.ngl.idea.game.core.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 游戏配置实体类
 */
@Data
@TableName("GM_GAME_CONFIG")
public class GmGameConfig {
    /**
     * 配置ID
     */
    @TableId(value = "CONFIG_ID", type = IdType.ASSIGN_UUID)
    private String configId;
    
    /**
     * 游戏ID
     */
    @TableField("GAME_ID")
    private String gameId;
    
    /**
     * 配置键
     */
    @TableField("CONFIG_KEY")
    private String configKey;
    
    /**
     * 配置值
     */
    @TableField("CONFIG_VALUE")
    private String configValue;
    
    /**
     * 配置描述
     */
    @TableField("CONFIG_DESCRIPTION")
    private String configDescription;
    
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
     * 是否销毁（0-未销毁，1-已销毁）
     */
    @TableField("DESTROY")
    private String destroy;
    
    /**
     * 是否有效（0-无效，1-有效）
     */
    @TableField("EFFECTIVE")
    private String effective;
} 