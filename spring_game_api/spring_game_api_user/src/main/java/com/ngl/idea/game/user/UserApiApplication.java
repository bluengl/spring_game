package com.ngl.idea.game.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages = {
    "com.ngl.idea.game",
})
public class UserApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApiApplication.class, args);
    }
}
