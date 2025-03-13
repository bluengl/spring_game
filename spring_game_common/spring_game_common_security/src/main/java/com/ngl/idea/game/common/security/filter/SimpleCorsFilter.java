package com.ngl.idea.game.common.security.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.ngl.idea.game.common.config.manager.ConfigManager;
import com.ngl.idea.game.common.config.properties.SecurityProperties;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 简单CORS过滤器，确保所有请求都添加CORS头
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@RefreshScope
public class SimpleCorsFilter implements Filter {

    @Autowired
    private ConfigManager configManager;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;
        // 获取请求头
        String origin = request.getHeader("Origin");
        String method = request.getHeader("Access-Control-Request-Method");
        String headers = request.getHeader("Access-Control-Request-Headers");

        // 设置CORS头
        SecurityProperties.CorsProperties cors = configManager.getSecurityConfig().getCors();

        if (cors != null) {
            if (origin != null) {  
                if (!cors.getAllowedOrigins().contains(origin)) {
                    throw new ServletException("Origin not allowed");
                }
            }
            if (method != null) {
                if (!cors.getAllowedMethods().contains(method)) {
                    throw new ServletException("Method not allowed");
                }
            }
            if (headers != null) {
                String[] headersArray = headers.split(",");
                for (String header : headersArray) {
                    List<String> allowedHeaders = cors.getAllowedHeaders();
                    AtomicBoolean isAllowed = new AtomicBoolean(false);
                    allowedHeaders.forEach(allowedHeader -> {
                        if (allowedHeader.equalsIgnoreCase(header)) {
                            isAllowed.set(true);
                        }
                    });
                    if (!isAllowed.get()) {
                        throw new ServletException("Headers not allowed");
                    }
                }
            }
        }

        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Methods", request.getHeader("Access-Control-Request-Method"));
        response.setHeader("Access-Control-Allow-Headers", request.getHeader("Access-Control-Request-Headers"));
        response.setHeader("Access-Control-Max-Age", request.getHeader("Access-Control-Max-Age"));
        response.setHeader("Access-Control-Allow-Credentials", request.getHeader("Access-Control-Allow-Credentials"));

        // 对于OPTIONS请求直接返回200状态码
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            chain.doFilter(req, res);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void destroy() {
    }
}
