# 游戏API配置中心使用说明

## 简介
配置中心模块（config）是一个基于Nacos的集中式配置管理模块，用于统一管理所有模块的配置信息。它支持配置的动态刷新，并提供了一套完整的配置访问机制。

## 功能特性
- 基于Nacos的配置管理
- 支持配置动态刷新
- 统一的配置访问接口
- 分类管理不同类型的配置
- 自动配置支持

## 配置分类
配置中心将配置分为以下几类：

### 1. 安全配置（security-config.yaml）
```yaml
game:
  security:
    jwt:
      secret: your-secret-key
      expiration: 86400
      header: Authorization
      prefix: Bearer
      allowRefresh: true
      refreshExpiration: 604800
    ignore:
      urls:
        - /api/auth/login
        - /api/auth/register
```

### 2. 数据库配置（database-config.yaml）
```yaml
game:
  database:
    url: jdbc:mysql://localhost:3306/game_db
    username: root
    password: root
    driverClassName: com.mysql.cj.jdbc.Driver
```

### 3. 通用配置（common-config.yaml）
```yaml
game:
  common:
    uploadPath: /data/uploads
    allowedOrigins:
      - http://localhost:8080
      - http://localhost:3000
    maxFileSize: 10485760
    maxRequestSize: 10485760
```

## 使用方法

### 1. 添加依赖
在需要使用配置的模块的`pom.xml`中添加依赖：
```xml
<dependency>
    <groupId>com.ngl.idea.game</groupId>
    <artifactId>spring_game_common_config</artifactId>
    <version>${project.version}</version>
</dependency>
```

### 2. 注入配置管理器
在需要使用配置的类中注入`ConfigManager`：
```java
@Autowired
private ConfigManager configManager;
```

### 3. 使用配置
```java
// 获取JWT配置
String secret = configManager.getJwtConfig().getSecret();
Long expiration = configManager.getJwtConfig().getExpiration();

// 获取忽略URL配置
String[] ignoreUrls = configManager.getIgnoreConfig().getUrls();

// 获取数据库配置
String dbUrl = configManager.getDatabaseConfig().getUrl();

// 获取通用配置
String uploadPath = configManager.getCommonConfig().getUploadPath();
```

### 4. 配置刷新
使用`@RefreshScope`注解的类会在配置更新时自动刷新：
```java
@Component
@RefreshScope
public class YourComponent {
    @Autowired
    private ConfigManager configManager;
    
    // 配置更新时，这里的值会自动更新
    private String someConfig = configManager.getCommonConfig().getUploadPath();
}
```

## Nacos配置说明

### 1. 配置项说明
- `data-id`：配置文件的ID
- `group`：配置分组，默认为DEFAULT_GROUP
- `refresh`：是否支持配置刷新

### 2. 配置更新
1. 登录Nacos控制台
2. 选择对应的配置项
3. 修改配置内容
4. 点击发布

### 3. 配置格式
- 支持YAML格式
- 所有配置项以`game`为根节点
- 配置项使用小写字母，单词间用连字符（-）分隔

## 注意事项
1. 确保Nacos服务已启动且可访问
2. 配置更新后，使用`@RefreshScope`注解的类会自动刷新
3. 敏感配置（如数据库密码）建议使用加密方式存储
4. 建议在开发环境和生产环境使用不同的配置组

## 常见问题

### 1. 配置无法刷新
- 检查类是否添加了`@RefreshScope`注解
- 检查Nacos配置是否正确发布
- 检查应用是否正确连接到Nacos

### 2. 配置读取失败
- 检查配置格式是否正确
- 检查配置项是否完整
- 检查配置路径是否正确

### 3. 启动失败
- 检查Nacos服务是否正常运行
- 检查bootstrap.yml中的配置是否正确
- 检查网络连接是否正常

## 最佳实践
1. 将敏感配置加密存储
2. 使用不同的配置组管理不同环境
3. 合理组织配置结构，避免配置项过多
4. 定期备份重要配置
5. 监控配置变更，及时发现问题 
