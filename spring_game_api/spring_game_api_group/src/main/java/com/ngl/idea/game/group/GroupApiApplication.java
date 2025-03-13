package com.ngl.idea.game.group;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages = {
    "com.ngl.idea.game",
    "com.ngl.idea.game.group",
    "com.ngl.idea.game.common.security",
    "com.ngl.idea.game.common.core",
    "com.ngl.idea.game.common.encryption",
    "com.ngl.idea.game.common.config"
})
public class GroupApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(GroupApiApplication.class, args);
    }
}
