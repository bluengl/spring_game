package com.ngl.idea.game.common.core.exception;

import com.ngl.idea.game.common.core.model.response.ResultCode;
import lombok.Getter;

/**
 * 业务异常类
 */
@Getter
public class BusinessException extends RuntimeException {

    /**
     * 错误码
     */
    private final Integer code;

    /**
     * 错误消息
     */
    private final String message;

    /**
     * 构造函数
     * @param resultCode 结果码
     */
    public BusinessException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }

    /**
     * 构造函数
     * @param code 错误码
     * @param message 错误消息
     */
    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    /**
     * 构造函数
     * @param message 错误消息
     */
    public BusinessException(String message) {
        super(message);
        this.code = ResultCode.INTERNAL_SERVER_ERROR.getCode();
        this.message = message;
    }
    
} 