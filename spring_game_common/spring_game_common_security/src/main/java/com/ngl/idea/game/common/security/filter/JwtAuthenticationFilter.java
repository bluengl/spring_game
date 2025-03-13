package com.ngl.idea.game.common.security.filter;

import com.ngl.idea.game.common.core.model.TokenUser;
import com.ngl.idea.game.common.core.util.ServiceLocator;
import com.ngl.idea.game.common.security.util.JwtUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        JwtUtils jwtUtils = (JwtUtils) ServiceLocator.getService("jwtUtils");
        // 获取token
        assert jwtUtils != null;
        String token = jwtUtils.getJwtFromRequest(request);

        // 处理配置信息中不需要认证的请求
        if (!jwtUtils.isIgnoreRequest(request)) {
            // 如果没有token
            if (token == null) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setCharacterEncoding(StandardCharsets.UTF_8.name());
                response.getWriter().write("{\"status\":401,\"error\":\"未授权\"}");
                return;
            } else if (!jwtUtils.validateJwtToken(token)) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setCharacterEncoding(StandardCharsets.UTF_8.name());
                response.getWriter().write("{\"status\":403,\"error\":\"token已失效\"}");
                return;
            }

            // 从token中提取用户信息
            TokenUser tokenUser = jwtUtils.extractTokenUser(token);

            // 装饰请求,添加用户信息
            HttpServletRequest decoratedRequest = new TokenUserHttpServletRequest(request, tokenUser);

            // 继续过滤器链
            filterChain.doFilter(decoratedRequest, response);
        } else {
            // 继续过滤器链
            if (token != null && jwtUtils.validateJwtToken(token)) {
                TokenUser tokenUser = jwtUtils.extractTokenUser(token);
                HttpServletRequest decoratedRequest = new TokenUserHttpServletRequest(request, tokenUser);
                filterChain.doFilter(decoratedRequest, response);
            } else {
                filterChain.doFilter(request, response);
            }
        }
    }
}
