package com.ngl.idea.game.common.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
@RefreshScope
@ConfigurationProperties(prefix = "spring.datasource")
public class DataSourceProperties {
    private String driverClassName;
    private String url;
    private String username;
    private String password;
    private HikariProperties hikari = new HikariProperties();

    public static class HikariProperties {
        private Integer minimumIdle;
        private Integer maximumPoolSize;
        private Boolean autoCommit;
        private Long idleTimeout;
        private String poolName;
        private Long maxLifetime;
        private Long connectionTimeout;
        private String connectionTestQuery;

        // Getters and Setters
        public Integer getMinimumIdle() {
            return minimumIdle;
        }

        public void setMinimumIdle(Integer minimumIdle) {
            this.minimumIdle = minimumIdle;
        }

        public Integer getMaximumPoolSize() {
            return maximumPoolSize;
        }

        public void setMaximumPoolSize(Integer maximumPoolSize) {
            this.maximumPoolSize = maximumPoolSize;
        }

        public Boolean getAutoCommit() {
            return autoCommit;
        }

        public void setAutoCommit(Boolean autoCommit) {
            this.autoCommit = autoCommit;
        }

        public Long getIdleTimeout() {
            return idleTimeout;
        }

        public void setIdleTimeout(Long idleTimeout) {
            this.idleTimeout = idleTimeout;
        }

        public String getPoolName() {
            return poolName;
        }

        public void setPoolName(String poolName) {
            this.poolName = poolName;
        }

        public Long getMaxLifetime() {
            return maxLifetime;
        }

        public void setMaxLifetime(Long maxLifetime) {
            this.maxLifetime = maxLifetime;
        }

        public Long getConnectionTimeout() {
            return connectionTimeout;
        }

        public void setConnectionTimeout(Long connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
        }

        public String getConnectionTestQuery() {
            return connectionTestQuery;
        }

        public void setConnectionTestQuery(String connectionTestQuery) {
            this.connectionTestQuery = connectionTestQuery;
        }
    }

    // Getters and Setters
    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public HikariProperties getHikari() {
        return hikari;
    }

    public void setHikari(HikariProperties hikari) {
        this.hikari = hikari;
    }
}
