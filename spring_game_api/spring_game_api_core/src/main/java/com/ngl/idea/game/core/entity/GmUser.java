package com.ngl.idea.game.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ngl.idea.game.common.core.model.TokenUser;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 用户实体类
 * 
 * <p>该类映射数据库表 GM_USER，用于存储用户基本信息。
 * 继承自 {@link TokenUser} 以支持 JWT token 相关功能。</p>
 *
 * <p>用户状态说明：
 * <ul>
 *     <li>effective: 1-有效，0-无效</li>
 *     <li>destroy: 1-已销毁，0-未销毁</li>
 * </ul>
 * </p>
 *
 * @author ngl
 * @version 1.0.0
 * @see TokenUser
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("GM_USER")
public class GmUser extends TokenUser {
    
    /**
     * 用户ID
     * <p>使用 UUID 自动生成</p>
     */
    @TableId(value = "USER_ID", type = IdType.ASSIGN_UUID)
    private String userId;

    /**
     * 第三方平台的用户标识
     * <p>用于关联第三方平台的用户信息</p>
     */
    @TableField("OPEN_ID")
    private String openId;

    /**
     * 用户来源
     * <p>标识用户注册的来源平台，如：
     * <ul>
     *     <li>SYSTEM - 系统注册</li>
     *     <li>WECHAT - 微信</li>
     *     <li>QQ - QQ</li>
     * </ul>
     * </p>
     */
    @TableField("USER_SOURCE")
    private String userSource;

    /**
     * 用户名
     * <p>用户登录时使用的唯一标识符</p>
     */
    @TableField("USER_NAME")
    private String username;

    /**
     * 密码
     * <p>使用 BCrypt 加密存储</p>
     */
    @TableField("PASSWORD")
    private String password;

    /**
     * 是否有效
     * <p>取值：
     * <ul>
     *     <li>1 - 有效</li>
     *     <li>0 - 无效</li>
     * </ul>
     * </p>
     */
    @TableField("EFFECTIVE")
    private String effective;

    /**
     * 是否销毁
     * <p>取值：
     * <ul>
     *     <li>1 - 已销毁</li>
     *     <li>0 - 未销毁</li>
     * </ul>
     * </p>
     */
    @TableField("DESTROY")
    private String destroy;

    /**
     * 创建时间
     * <p>记录用户创建的时间戳</p>
     */
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    /**
     * 更新时间
     * <p>记录用户信息最后一次更新的时间戳</p>
     */
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;
} 