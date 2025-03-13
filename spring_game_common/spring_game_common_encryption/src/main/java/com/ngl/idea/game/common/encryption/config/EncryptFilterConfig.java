package com.ngl.idea.game.common.encryption.config;

import com.ngl.idea.game.common.encryption.filter.DecryptRequestFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
public class EncryptFilterConfig {

    @Bean
    public FilterRegistrationBean<DecryptRequestFilter> decryptRequestFilterServletRegistrationBean() {
        DecryptRequestFilter decryptRequestFilter = new DecryptRequestFilter();
        FilterRegistrationBean<DecryptRequestFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(decryptRequestFilter);
        bean.setName("decryptRequestFilter");
        bean.addUrlPatterns("/*");
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }

}
