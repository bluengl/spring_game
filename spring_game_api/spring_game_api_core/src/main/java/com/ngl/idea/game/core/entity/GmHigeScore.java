package com.ngl.idea.game.core.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("GM_HIGE_SCORE")
public class GmHigeScore {
    /**
     * 分数ID
     */
    @TableId(value = "SCORE_ID", type = IdType.ASSIGN_UUID)
    private String scoreId;

    /**
     * 游戏ID
     */
    @TableField("GAME_ID")
    private String gameId;

    /**
     * 用户ID
     */
    @TableField("USER_ID")
    private String userId;

    /**
     * 用户名
     */
    @TableField("USER_NAME")
    private String username;

    /**
     * 分数
     */
    @TableField("GAME_LEVEL")
    private Integer gameLevel;

    /**
     * 分数
     */
    @TableField("GAME_SCORE")
    private Integer gameScore;

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
}
