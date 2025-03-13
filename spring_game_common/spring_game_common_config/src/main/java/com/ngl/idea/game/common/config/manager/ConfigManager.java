package com.ngl.idea.game.common.config.manager;

import com.ngl.idea.game.common.config.properties.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * 配置管理器
 * 统一管理所有配置属性的访问
 */
@Component
@RefreshScope
public class ConfigManager {

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private DataSourceProperties dataSourceProperties;

    @Autowired
    private CommonProperties commonProperties;

    @Autowired
    private EncryptionProperties encryptionProperties;

    @Autowired
    private MybatisPlusProperties mybatisPlusProperties;

    /**
     * 获取安全配置
     */
    public SecurityProperties getSecurityConfig() {
        return securityProperties;
    }

    /**
     * 获取数据源配置
     */
    public DataSourceProperties getDataSourceConfig() {
        return dataSourceProperties;
    }

    /**
     * 获取通用配置
     */
    public CommonProperties getCommonConfig() {
        return commonProperties;
    }

    /**
     * 获取JWT配置
     */
    public SecurityProperties.JwtProperties getJwtConfig() {
        return securityProperties.getJwt();
    }

    /**
     * 获取安全忽略URL配置
     */
    public SecurityProperties.IgnoreProperties getIgnoreConfig() {
        return securityProperties.getIgnore();
    }

    /**
     * 获取加密配置
     */
    public EncryptionProperties getEncryptionConfig() {
        return encryptionProperties;
    }

    /**
     * 获取Hikari连接池配置
     */
    public DataSourceProperties.HikariProperties getHikariConfig() {
        return dataSourceProperties.getHikari();
    }

    /**
     * 获取Mybatis Plus配置
     */
    public MybatisPlusProperties getMybatisPlusConfig() {
        return mybatisPlusProperties;
    }

}
