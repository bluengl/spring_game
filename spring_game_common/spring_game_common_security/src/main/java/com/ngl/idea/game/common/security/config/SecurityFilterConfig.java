package com.ngl.idea.game.common.security.config;

import com.ngl.idea.game.common.security.filter.JwtAuthenticationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
public class SecurityFilterConfig {

    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> jwtAuthenticationFilterServletRegistrationBean() {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter();
        FilterRegistrationBean<JwtAuthenticationFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(jwtAuthenticationFilter);
        bean.setName("jwtAuthenticationFilter");
        bean.addUrlPatterns("/*");
        bean.setOrder(Ordered.LOWEST_PRECEDENCE);
        return bean;
    }

}
