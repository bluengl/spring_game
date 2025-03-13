package com.ngl.idea.game.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ngl.idea.game.common.core.model.TokenUser;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("GM_USER")
public class GmUser extends TokenUser {
    @TableId(value = "USER_ID", type = IdType.ASSIGN_UUID)
    private String userId;
    @TableField("OPEN_ID")
    private String openId;
    @TableField("USER_SOURCE")
    private String userSource;
    @TableField("USER_NAME")
    private String username;
    @TableField("PASSWORD")
    private String password;
    @TableField("EFFECTIVE")
    private String effective;
    @TableField("DESTROY")
    private String destroy;
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
}
