package com.ngl.idea.game.common.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 配置自动装配类
 */
@Configuration
@ComponentScan("com.ngl.idea.game")
@EnableConfigurationProperties
public class ConfigAutoConfiguration {
}
