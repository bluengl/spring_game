package com.ngl.idea.game.core.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.ngl.idea.game.common.core.model.response.ApiResponse;
import com.ngl.idea.game.common.security.filter.TokenUserHttpServletRequest;
import com.ngl.idea.game.common.core.exception.BusinessException;
import com.ngl.idea.game.common.core.model.TokenUser;

public class GameBaseController {

    /**
     * 获取当前请求对象
     * @return TokenUserHttpServletRequest
     */
    protected HttpServletRequest getRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new BusinessException("获取请求上下文失败");
        }
        
        HttpServletRequest request = attributes.getRequest();
        if (!(request instanceof TokenUserHttpServletRequest)) {
            throw new BusinessException("非法的请求类型");
        }
        
        return request;
    }

    /**
     * 获取当前用户ID
     * @return String 用户ID
     */
    protected String getUserId() {
        return getUserInfo().getUserId();
    }

    /**
     * 获取当前用户信息
     * @return TokenUser 用户信息
     */
    protected TokenUser getUserInfo() {
        return ((TokenUserHttpServletRequest) getRequest()).getTokenUser();
    }
} 