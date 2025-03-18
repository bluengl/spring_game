package com.ngl.idea.game.common.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.ngl.idea.game.common.config.manager.ConfigManager;
import com.ngl.idea.game.common.config.properties.SecurityProperties;


/**
 * CORS跨域配置
 */
@Configuration
@RefreshScope
public class CorsConfig implements WebMvcConfigurer {

    @Autowired
    private ConfigManager configManager;
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        SecurityProperties.CorsProperties corsProperties = configManager.getSecurityConfig().getCors();
        // 如果没有配置信息，则使用默认配置
        if (corsProperties == null) {
            registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
        } else {
            registry.addMapping("/**")
                .allowedOrigins(corsProperties.getAllowedOrigins().toArray(new String[0]))
                .allowedMethods(corsProperties.getAllowedMethods().toArray(new String[0]))
                .allowedHeaders(corsProperties.getAllowedHeaders().toArray(new String[0]))
                .allowCredentials(corsProperties.getAllowCredentials())
                .maxAge(corsProperties.getMaxAge());
        }
    }

    // /**
    //  * 配置CORS过滤器，设置高优先级确保它在其他过滤器之前执行
    //  */
    // @Bean
    // @Order(Ordered.HIGHEST_PRECEDENCE)
    // public CorsFilter corsFilter() {
    //     SecurityProperties.CorsProperties corsProperties = configManager.getSecurityConfig().getCors();
    //     // 创建CORS配置
    //     CorsConfiguration corsConfiguration = new CorsConfiguration();

    //     // 如果允许的源为空，添加一个默认配置
    //     if (corsProperties.getAllowedOrigins() == null || corsProperties.getAllowedOrigins().isEmpty()) {
    //         corsConfiguration.addAllowedOrigin("*");
    //     } else {
    //         corsConfiguration.setAllowedOriginPatterns(corsProperties.getAllowedOrigins());
    //     }

    //     List<String> exposedHeaders = corsProperties.getExposedHeaders();
    //     List<String> allowedHeaders = corsProperties.getAllowedHeaders();
    //     String header = configManager.getSecurityConfig().getJwt().getHeader();

    //     // 确保列表不为空
    //     if (exposedHeaders == null) {
    //         exposedHeaders = Collections.singletonList(header);
    //     } else {
    //         exposedHeaders.add(header);
    //     }

    //     if (allowedHeaders == null) {
    //         allowedHeaders = Collections.singletonList("*");
    //     } else {
    //         allowedHeaders.add(header);
    //     }

    //     // 处理重复
    //     exposedHeaders = exposedHeaders.stream().distinct().collect(Collectors.toList());
    //     allowedHeaders = allowedHeaders.stream().distinct().collect(Collectors.toList());

    //     // 允许暴露的请求头
    //     corsConfiguration.setExposedHeaders(exposedHeaders);

    //     // 允许所有请求头
    //     corsConfiguration.setAllowedHeaders(allowedHeaders);

    //     // 确保OPTIONS请求被正确处理
    //     if (corsProperties.getAllowedMethods() == null || corsProperties.getAllowedMethods().isEmpty()) {
    //         corsConfiguration.addAllowedMethod("*");
    //     } else {
    //         // 确保包含OPTIONS方法
    //         List<String> methods = corsProperties.getAllowedMethods();
    //         if (!methods.contains("OPTIONS")) {
    //             methods.add("OPTIONS");
    //         }
    //         corsConfiguration.setAllowedMethods(methods);
    //     }

    //     // 允许发送Cookie
    //     corsConfiguration.setAllowCredentials(corsProperties.getAllowCredentials());

    //     // 设置预检请求的有效期，单位为秒
    //     corsConfiguration.setMaxAge(corsProperties.getMaxAge());

    //     // 配置CORS路径映射
    //     UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    //     // 对所有路径应用CORS配置
    //     source.registerCorsConfiguration("/**", corsConfiguration);

    //     // 返回CORS过滤器
    //     return new CorsFilter(source);
    // }
}
