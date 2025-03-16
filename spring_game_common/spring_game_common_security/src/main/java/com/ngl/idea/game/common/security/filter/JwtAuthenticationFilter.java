package com.ngl.idea.game.common.security.filter;

import com.ngl.idea.game.common.core.exception.BusinessException;
import com.ngl.idea.game.common.core.model.TokenUser;
import com.ngl.idea.game.common.core.model.response.ResultCode;
import com.ngl.idea.game.common.core.util.RedisUtil;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        JwtUtils jwtUtils = (JwtUtils) ServiceLocator.getService("jwtUtils");
        RedisUtil redisUtil = (RedisUtil) ServiceLocator.getService("redisUtil");
        // 获取token
        assert jwtUtils != null;
        String token = jwtUtils.getJwtFromRequest(request);

        // 处理配置信息中不需要认证的请求
        if (!jwtUtils.isIgnoreRequest(request)) {
            // 如果没有token
            if (token == null) {
                defaultResponse(response, "401", "未授权,请先登录");
                return;
            }
            // 获取请求的URI
            String requestURI = request.getRequestURI();
            if (!jwtUtils.validateJwtToken(token)) {
                if (requestURI.contains("/refreshToken")) {
                    defaultResponse(response, "401", "refreshToken已失效,请重新登录");
                } else {
                    defaultResponse(response, "403", "token已失效");
                }
                return;
            }

            // 从token中提取用户信息
            TokenUser tokenUser = jwtUtils.extractTokenUser(token);
            if (tokenUser == null) {
                defaultResponse(response, "401", "token解析失败,请重新登录");
                return;
            }
            String tokenType = tokenUser.getTokenType();
            String tokenCode = tokenUser.getTokenCode();
            assert redisUtil != null;
            String redisTokenCode = (String) redisUtil.get("userTokenCode-" + tokenUser.getUserId());
            // 判断请求是不是refreshToken
            if ("refreshToken".equals(tokenType)) {
                if (!requestURI.contains("/refreshToken")) {
                    defaultResponse(response, "401", "错误使用refreshToken,请重新登录");
                    return;
                } else {
                    redisTokenCode = (String) redisUtil.get("userRefreshTokenCode-" + tokenUser.getUserId());
                    if (tokenCode == null || !tokenCode.equals(redisTokenCode)) {
                        defaultResponse(response, "401", "refreshToken已失效,请重新登录");
                        return;
                    }
                }
            } else if (!jwtUtils.validateJwtToken(token) || tokenCode == null || !tokenCode.equals(redisTokenCode)) {
                defaultResponse(response, "403", "token已失效");
                return;
            }

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

    private void defaultResponse(HttpServletResponse response, String code, String error) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write("{\"status\":" + code + ",\"error\":\"" + error + "\",\"timestamp\": \"" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "\"}");
    }

}
