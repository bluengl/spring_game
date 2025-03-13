# Spring Game 后端服务

这是一个基于 Spring Boot 的游戏后端服务项目，提供用户认证、游戏逻辑、在线状态等功能。

## 项目架构

项目采用模块化设计，主要包含以下模块：

### API 模块 (spring_game_api)
- **spring_game_api_base**: 基础API服务，处理用户认证、系统配置等
- **spring_game_api_game**: 游戏核心逻辑API
- **spring_game_api_group**: 群组相关API
- **spring_game_api_online**: 在线状态管理API
- **spring_game_api_user**: 用户管理API

### 公共模块 (spring_game_common)
- **spring_game_common_config**: 统一配置管理
- **spring_game_common_core**: 核心工具类和通用功能
- **spring_game_common_encryption**: 加密解密功能
- **spring_game_common_security**: 安全认证和授权

## 主要功能

1. **用户认证**
   - 登录/注册
   - JWT token 认证
   - 权限管理

2. **安全特性**
   - CORS 跨域支持
   - 请求加密/解密
   - 防SQL注入
   - XSS防护

3. **系统配置**
   - 动态配置管理
   - 环境隔离
   - 统一配置中心

## 开发环境要求

- JDK 11+
- Maven 3.6+
- MySQL 8.0+
- Redis (可选)

## 快速开始

1. **克隆项目**
```bash
git clone git@github.com:bluengl/spring_game.git
cd spring_game
```

2. **配置数据库**
- 创建数据库
- 修改 `application-dev.yml` 中的数据库配置

3. **编译打包**
```bash
mvn clean package
```

4. **运行服务**
```bash
java -jar spring_game_api_base/target/spring_game_api_base.jar
```

## API 文档

主要API端点：

### 基础API (/api/base/*)
- `POST /api/base/login`: 用户登录
- `POST /api/base/register`: 用户注册
- `GET /api/base/getSystemConfig`: 获取系统配置
- `GET /api/base/getOpenId`: 获取OpenID

### 游戏API (/api/game/*)
- 待补充

### 群组API (/api/group/*)
- 待补充

### 在线API (/api/online/*)
- 待补充

## 安全配置

1. **CORS配置**
```yaml
security:
  cors:
    enabled: true
    allowedOrigins: 
      - "http://localhost:8080"
    allowedMethods:
      - GET
      - POST
      - PUT
      - DELETE
      - OPTIONS
```

2. **JWT配置**
```yaml
security:
  jwt:
    secret: your-secret-key
    expiration: 86400
```

## 注意事项

1. 开发环境配置文件为 `application-dev.yml`
2. 生产环境请修改相应的配置和密钥
3. 建议使用HTTPS进行安全传输
4. 定期更新依赖包版本，修复安全漏洞

## 贡献指南

1. Fork 本仓库
2. 创建特性分支
3. 提交代码
4. 创建 Pull Request

## 许可证

[MIT License](LICENSE)
