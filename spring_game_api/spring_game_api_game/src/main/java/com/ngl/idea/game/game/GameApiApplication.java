package com.ngl.idea.game.game;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages = {
    "com.ngl.idea.game",
})
public class GameApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(GameApiApplication.class, args);
    }
}
