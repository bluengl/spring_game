package com.ngl.idea.game.base;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import com.ngl.idea.game.common.security.config.CorsConfig;

/**
 * Base API 应用程序入口
 */
@SpringBootApplication
@ComponentScan(basePackages = {
    "com.ngl.idea.game",
    "com.ngl.idea.game.base",
    "com.ngl.idea.game.common.security",
    "com.ngl.idea.game.common.core",
    "com.ngl.idea.game.common.encryption",
    "com.ngl.idea.game.common.config"
})
@Import(CorsConfig.class)
public class BaseApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(BaseApiApplication.class, args);
    }
}
