package com.ngl.idea.game.common.core.exception;

import com.ngl.idea.game.common.core.model.response.ApiResponse;
import com.ngl.idea.game.common.core.model.response.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理自定义业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public ApiResponse<?> handleBusinessException(BusinessException e, HttpServletRequest request) {
        log.error("业务异常: {}", e.getMessage());
        return ApiResponse.error(e.getCode(), e.getMessage())
                .path(request.getRequestURI());
    }

    /**
     * 处理参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        BindingResult bindingResult = e.getBindingResult();
        String message = bindingResult.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));
        log.error("参数校验异常: {}", message);
        return ApiResponse.error(ResultCode.PARAM_ERROR.getCode(), message)
                .path(request.getRequestURI());
    }

    /**
     * 处理参数绑定异常
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleBindException(BindException e, HttpServletRequest request) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));
        log.error("参数绑定异常: {}", message);
        return ApiResponse.error(ResultCode.PARAM_ERROR.getCode(), message)
                .path(request.getRequestURI());
    }

    /**
     * 处理约束违反异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleConstraintViolationException(ConstraintViolationException e, HttpServletRequest request) {
        String message = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("; "));
        log.error("约束违反异常: {}", message);
        return ApiResponse.error(ResultCode.PARAM_ERROR.getCode(), message)
                .path(request.getRequestURI());
    }

    /**
     * 处理参数类型不匹配异常
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        log.error("参数类型不匹配异常: {}", e.getMessage());
        return ApiResponse.error(ResultCode.PARAM_ERROR.getCode(), "参数类型不匹配: " + e.getName())
                .path(request.getRequestURI());
    }

    /**
     * 处理缺少请求参数异常
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleMissingServletRequestParameterException(MissingServletRequestParameterException e, HttpServletRequest request) {
        log.error("缺少请求参数异常: {}", e.getMessage());
        return ApiResponse.error(ResultCode.PARAM_ERROR.getCode(), "缺少必要参数: " + e.getParameterName())
                .path(request.getRequestURI());
    }

    /**
     * 处理资源不存在异常
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<?> handleNoHandlerFoundException(NoHandlerFoundException e, HttpServletRequest request) {
        log.error("资源不存在异常: {}", e.getMessage());
        return ApiResponse.error(ResultCode.NOT_FOUND.getCode(), "资源不存在: " + e.getRequestURL())
                .path(request.getRequestURI());
    }

    /**
     * 处理请求方法不支持异常
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ApiResponse<?> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        log.error("请求方法不支持异常: {}", e.getMessage());
        return ApiResponse.error(ResultCode.METHOD_NOT_ALLOWED.getCode(), "不支持的请求方法: " + e.getMethod())
                .path(request.getRequestURI());
    }

    /**
     * 处理所有其他异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<?> handleException(Exception e, HttpServletRequest request) {
        log.error("系统异常: ", e);
        return ApiResponse.error(ResultCode.INTERNAL_SERVER_ERROR.getCode(), "服务器内部错误: " + e.getMessage())
                .path(request.getRequestURI());
    }
} 