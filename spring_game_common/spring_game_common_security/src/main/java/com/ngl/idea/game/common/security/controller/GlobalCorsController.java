package com.ngl.idea.game.common.security.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 全局CORS预检请求处理控制器
 */
@RestController
public class GlobalCorsController {

    // @Autowired
    // private ConfigManager configManager;

    /**
     * 处理所有OPTIONS请求
     */
    @RequestMapping(value = "/**", method = RequestMethod.OPTIONS)
    public void handleOptions(HttpServletResponse response, HttpServletRequest request) {
        // SecurityProperties.CorsProperties cors = configManager.getSecurityConfig().getCors();
        // 设置CORS头
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Methods", request.getHeader("Access-Control-Request-Method"));
        response.setHeader("Access-Control-Allow-Headers", request.getHeader("Access-Control-Request-Headers"));
        response.setHeader("Access-Control-Max-Age", request.getHeader("Access-Control-Max-Age"));
        response.setHeader("Access-Control-Allow-Credentials", request.getHeader("Access-Control-Allow-Credentials"));
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
