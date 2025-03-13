package com.ngl.idea.game.common.core.controller;

import com.ngl.idea.game.common.core.model.response.ApiResponse;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 自定义错误控制器
 * 替代Spring Boot默认的错误控制器，提供统一的错误响应格式
 */
@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
public class CustomErrorController extends AbstractErrorController {

    /**
     * 构造函数
     * @param errorAttributes 错误属性
     */
    public CustomErrorController(ErrorAttributes errorAttributes) {
        super(errorAttributes);
    }

    /**
     * 处理所有错误
     */
    @RequestMapping
    @ResponseBody
    public ApiResponse<Object> error(HttpServletRequest request) {
        // 获取错误属性
        Map<String, Object> errorAttributes = getErrorAttributes(request, 
                ErrorAttributeOptions.of(ErrorAttributeOptions.Include.MESSAGE));
        
        // 提取错误信息
        int status = (int) errorAttributes.get("status");
        String error = (String) errorAttributes.get("error");
        String path = (String) errorAttributes.get("path");
        
        // 创建统一响应
        ApiResponse<Object> response = ApiResponse.error(status, error);
        response.setPath(path);
        
        return response;
    }
} 