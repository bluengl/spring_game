package com.ngl.idea.game.common.core.model.response;

/**
 * 响应结果码枚举
 */
public enum ResultCode {

    SUCCESS(200, "操作成功"),

    PARAM_ERROR(400, "参数错误"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "资源不存在"),
    METHOD_NOT_ALLOWED(405, "方法不允许"),

    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),
    SERVICE_UNAVAILABLE(503, "服务不可用"),

    /* 业务错误码 */
    USER_NOT_FOUND(1001, "用户不存在"),
    USER_PASSWORD_ERROR(1002, "用户密码错误"),
    USER_ACCOUNT_LOCKED(1003, "用户账号已锁定"),
    USER_ACCOUNT_INVALID(1004, "用户账号已作废"),
    USER_ALREADY_EXIST(1005, "用户已存在"),
    OPEN_ID_ALREADY_BOUND(1006, "OpenID已绑定其他账号"),

    TOKEN_INVALID(2001, "无效的Token"),
    TOKEN_EXPIRED(2002, "Token已过期"),
    TOKEN_PARSE_ERROR(2003, "Token解析失败"),
    TOKEN_GENERATE_ERROR(2004, "Token生成失败"),

    GAME_NOT_FOUND(3001, "游戏不存在"),
    GAME_ALREADY_EXIST(3002, "游戏已存在"),

    GROUP_NOT_FOUND(4001, "群组不存在"),
    GROUP_ALREADY_EXIST(4002, "群组已存在"),
    NOT_GROUP_MEMBER(4003, "不是群组成员"),
    NOT_GROUP_OWNER(4004, "不是群组拥有者"),

    /* 加密相关错误码 */
    ENCRYPT_ERROR(5001, "数据加密失败"),
    DECRYPT_ERROR(5002, "数据解密失败");

    private final Integer code;
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
