package com.ngl.idea.game.common.core.model.response;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 统一API响应结果封装
 */
@Data
public class ApiResponse<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 当前时间戳
     */
    private String timestamp;

    /**
     * 状态码
     */
    private Integer status;

    /**
     * 错误信息
     */
    private String error;

    /**
     * 请求路径
     */
    private String path;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 私有构造函数
     */
    private ApiResponse() {
        this.timestamp = ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    /**
     * 成功响应
     * @param data 响应数据
     * @param <T> 数据类型
     * @return ApiResponse
     */
    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setData(data);
        return response;
    }

    /**
     * 成功响应
     * @param <T> 数据类型
     * @return ApiResponse
     */
    public static <T> ApiResponse<T> success() {
        return success(null);
    }

    /**
     * 失败响应
     * @param status 状态码
     * @param error 错误信息
     * @param <T> 数据类型
     * @return ApiResponse
     */
    public static <T> ApiResponse<T> error(Integer status, String error) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setStatus(status);
        response.setError(error);
        return response;
    }

    /**
     * 失败响应
     * @param resultCode 结果码
     * @param <T> 数据类型
     * @return ApiResponse
     */
    public static <T> ApiResponse<T> error(ResultCode resultCode) {
        return error(resultCode.getCode(), resultCode.getMessage());
    }

    /**
     * 服务器内部错误
     * @param error 错误信息
     * @param <T> 数据类型
     * @return ApiResponse
     */
    public static <T> ApiResponse<T> error(String error) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR.value(), error);
    }

    /**
     * 设置请求路径
     * @param path 路径
     * @return ApiResponse
     */
    public ApiResponse<T> path(String path) {
        this.setPath(path);
        return this;
    }
} 