package com.ngl.idea.game.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import com.ngl.idea.game.common.security.config.CorsConfig;

/**
 * 用户服务启动类
 */
@SpringBootApplication(scanBasePackages = "com.ngl.idea.game")
@MapperScan("com.ngl.idea.game.core.mapper")
@Import(CorsConfig.class)
public class UserApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApiApplication.class, args);
    }
}
