package com.ngl.idea.game.online;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages = {
    "com.ngl.idea.game",
})
public class OnlineApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(OnlineApiApplication.class, args);
    }
}
