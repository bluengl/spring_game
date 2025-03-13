package com.ngl.idea.game.common.core.advice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ngl.idea.game.common.core.model.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 响应结果处理器
 * 在响应体返回之前进行处理，设置响应格式
 * 注意：优先级设置为0，确保在加密模块之前处理
 */
@Slf4j
@RestControllerAdvice
@Order(0)
public class ResponseResultHandler implements ResponseBodyAdvice<Object> {

    private final ObjectMapper objectMapper;

    @Autowired
    public ResponseResultHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 如果接口返回的类型本身就是ApiResponse那么无需进行处理
        return !returnType.getParameterType().equals(ApiResponse.class);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                 Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                 ServerHttpRequest request, ServerHttpResponse response) {
        // 获取请求路径
        HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
        String path = servletRequest.getRequestURI();
        
        // 如果返回的结果是ApiResponse类型，直接设置path并返回
        if (body instanceof ApiResponse) {
            ApiResponse<?> apiResponse = (ApiResponse<?>) body;
            apiResponse.setPath(path);
            return apiResponse;
        }
        
        // 处理错误响应
        // 判断是否是Spring Boot的错误响应（检查是否是/error路径以及响应体格式）
        if ("/error".equals(path) && body instanceof Map) {
            Map<String, Object> errorMap = (Map<String, Object>) body;
            // 检查是否包含Spring Boot错误响应的特征字段
            if (errorMap.containsKey("timestamp") && errorMap.containsKey("status") && 
                errorMap.containsKey("error") && errorMap.containsKey("path")) {
                
                // 从错误响应中提取信息
                int status = ((Integer) errorMap.get("status"));
                String error = (String) errorMap.get("error");
                String actualPath = (String) errorMap.get("path");
                
                // 创建适当的错误响应
                ApiResponse<Object> errorResponse = ApiResponse.error(status, error);
                errorResponse.setPath(actualPath); // 使用原始请求路径，而非/error
                return errorResponse;
            }
        }
        
        // 创建标准响应对象
        ApiResponse<Object> apiResponse = ApiResponse.success(body);
        apiResponse.setPath(path);
        
        // 处理String类型返回值特殊情况
        if (returnType.getGenericParameterType().equals(String.class)) {
            log.debug("返回类型是String，进行特殊处理");
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            try {
                // 将ApiResponse对象转换为JSON字符串返回
                return objectMapper.writeValueAsString(apiResponse);
            } catch (JsonProcessingException e) {
                log.error("将响应转换为JSON字符串时发生错误", e);
                return "{\"timestamp\":\"" + apiResponse.getTimestamp() + 
                      "\",\"status\":500,\"error\":\"内部服务器错误\",\"path\":\"" + 
                      path + "\",\"data\":null}";
            }
        }
        
        return apiResponse;
    }
} 