package com.ngl.idea.game.game;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import com.ngl.idea.game.common.security.config.CorsConfig;


@SpringBootApplication(scanBasePackages = "com.ngl.idea.game")
@MapperScan("com.ngl.idea.game.core.mapper")
@Import(CorsConfig.class)
public class GameApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(GameApiApplication.class, args);
    }
}
