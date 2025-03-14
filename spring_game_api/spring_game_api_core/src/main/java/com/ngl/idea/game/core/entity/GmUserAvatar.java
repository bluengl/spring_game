package com.ngl.idea.game.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户头像实体类
 */
@Data
@TableName("GM_USER_AVATAR")
public class GmUserAvatar {

    /**
     * 头像ID
     */
    @TableId(value = "AVATAR_ID", type = IdType.ASSIGN_UUID)
    private String avatarId;

    /**
     * 用户ID
     */
    @TableField("USER_ID")
    private String userId;

    /**
     * Base64编码的头像数据
     */
    @TableField("BASE64")
    private String base64;

    /**
     * 版本号
     */
    @TableField("VERSION")
    private String version;

    /**
     * 状态
     */
    @TableField("STATUS")
    private String status;

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
     * 激活
     */
    @TableField("ACTIVE")
    private String active;
}
