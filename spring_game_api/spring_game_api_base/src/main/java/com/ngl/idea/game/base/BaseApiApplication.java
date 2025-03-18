package com.ngl.idea.game.base;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Import;
import com.ngl.idea.game.common.security.config.CorsConfig;

/**
 * Base API 应用程序入口
 */
@SpringBootApplication(scanBasePackages = "com.ngl.idea.game")
@MapperScan("com.ngl.idea.game.core.mapper")
@RefreshScope
@Import(CorsConfig.class)
public class BaseApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(BaseApiApplication.class, args);
    }
}
