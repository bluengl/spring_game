package com.ngl.idea.game.common.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
@RefreshScope
@ConfigurationProperties(prefix = "mybatis-plus")
public class MybatisPlusProperties {
    private String mapperLocations;
    private String typeAliasesPackage;
    private Configuration configuration = new Configuration();

    public static class Configuration {
        private Boolean mapUnderscoreToCamelCase;
        private String logImpl;

        public Boolean getMapUnderscoreToCamelCase() {
            return mapUnderscoreToCamelCase;
        }

        public void setMapUnderscoreToCamelCase(Boolean mapUnderscoreToCamelCase) {
            this.mapUnderscoreToCamelCase = mapUnderscoreToCamelCase;
        }

        public String getLogImpl() {
            return logImpl;
        }

        public void setLogImpl(String logImpl) {
            this.logImpl = logImpl;
        }
    }

    public String getMapperLocations() {
        return mapperLocations;
    }

    public void setMapperLocations(String mapperLocations) {
        this.mapperLocations = mapperLocations;
    }

    public String getTypeAliasesPackage() {
        return typeAliasesPackage;
    }

    public void setTypeAliasesPackage(String typeAliasesPackage) {
        this.typeAliasesPackage = typeAliasesPackage;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }
}
