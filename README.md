# Spring Game Framework

## 项目简介
Spring Game Framework 是一个基于Spring Boot的游戏服务框架，提供了一系列游戏服务开发所需的基础功能和通用组件。

## 项目结构
```
spring_game/
├── spring_game_common/                # 通用模块
│   ├── spring_game_common_core/       # 核心模块
│   │   ├── 通用响应封装
│   │   ├── 全局异常处理
│   │   ├── 通用工具类
│   │   ├── 基础注解
│   │   └── 常量定义
│   ├── spring_game_common_config/     # 配置模块
│   │   ├── 配置管理
│   │   ├── 配置刷新
│   │   └── 配置验证
│   └── spring_game_common_encryption/ # 加密模块
│       ├── 请求解密
│       ├── 响应加密
│       └── 密钥管理
└── spring_game_api/                # 服务模块
    ├── 游戏服务
    ├── 管理服务
    └── 网关服务
```

## 技术栈
- JDK 8
- Spring Boot 2.7.18
- Spring Cloud 2021.0.8
- MySQL 5.7.22
- MyBatis-Plus 3.5.5
- Lombok 1.18.30
- Maven 3.8+

## 模块说明

### 1. spring_game_common_core
核心模块，提供基础功能和通用组件：
- 统一响应封装
- 全局异常处理
- 通用工具类
- 基础注解
- 常量定义

### 2. spring_game_common_config
配置模块，提供配置管理功能：
- 配置加载和刷新
- 配置验证
- 配置加密
- 配置热更新

### 3. spring_game_common_encryption
加密模块，提供数据加解密功能：
- 请求解密
- 响应加密
- 密钥管理
- 加密算法支持

### api模块
- 包含所有业务服务模块
- 每个子模块都是独立的Spring Boot应用
- 共享common模块的功能

#### base-api（基础服务）
- 提供基础功能接口
- 包含登录、注册、获取OpenId等接口

#### game-api（游戏服务）
- 提供游戏相关接口
- 包含游戏列表、游戏成绩、排行榜等接口

#### user-api（用户服务）
- 提供用户相关接口
- 包含用户信息查询等接口

#### online-api（在线服务）
- 提供在线用户相关接口
- 包含在线用户列表等接口

#### group-api（群组服务）
- 提供群组相关接口
- 包含群组管理、群组消息等接口

## 开发环境要求

- JDK 8
- Maven 3.8+
- MySQL 5.7.22
- IDE：推荐使用 IntelliJ IDEA

## 配置说明

每个服务模块的配置文件（application.yml）包含：
- 服务端口配置
- 数据库连接配置
- MyBatis-Plus配置

## 接口说明

### 基础接口 (base-api)
- GET /api/base/getOpenId - 获取OpenId
- POST /api/base/login - 登录
- POST /api/base/register - 注册
- POST /api/base/logout - 退出登录

### 用户接口 (user-api)
- GET /api/user/userInfo - 获取用户信息

### 游戏接口 (game-api)
- GET /api/game/gameList - 获取游戏列表
- GET /api/game/gameBestScore - 获取游戏最佳成绩
- GET /api/game/gameRank - 获取游戏排行榜

### 在线接口 (online-api)
- GET /api/online/onlineList - 获取在线用户列表

### 群组接口 (group-api)
- GET /api/group/groupList - 获取群组列表
- GET /api/group/groupDetail - 获取群组详情
- POST /api/group/createGroup - 创建群组
- POST /api/group/joinGroup - 加入群组
- POST /api/group/exitGroup - 退出群组
- POST /api/group/deleteGroup - 解散群组
- GET /api/group/groupMemberList - 获取群组成员列表
- GET /api/group/groupMessageList - 获取群组消息列表
- POST /api/group/sendGroupMessage - 发送群组消息

## 快速开始

### 1. 环境要求
- JDK 1.8+
- Maven 3.6+
- Spring Boot 2.7.0+

### 2. 添加依赖
```xml
<dependency>
    <groupId>com.ngl.idea.game</groupId>
    <artifactId>spring_game_common_core</artifactId>
    <version>${project.version}</version>
</dependency>
```

### 3. 配置说明
```yaml
game:
  encryption:
    enabled: true
    ignore:
      urls:
        - /api/v1/auth/login
```

## 使用示例

### 1. 统一响应
```java
@RestController
@RequestMapping("/api")
public class UserController {
    
    @GetMapping("/user/{id}")
    public ApiResponse<User> getUser(@PathVariable Long id) {
        User user = userService.getById(id);
        return ApiResponse.success(user);
    }
}
```

### 2. 异常处理
```java
@Service
public class UserService {
    
    public User getUser(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        return user;
    }
}
```

### 3. 加密使用
```java
@RestController
@RequestMapping("/api")
public class OrderController {
    
    @PostMapping("/order")
    public ApiResponse<Order> createOrder(@RequestBody OrderDTO orderDTO) {
        // 请求会自动解密
        Order order = orderService.createOrder(orderDTO);
        // 响应会自动加密
        return ApiResponse.success(order);
    }
}
```

## 开发指南

### 1. 代码规范
- 遵循阿里巴巴Java开发手册
- 使用统一的代码格式化工具
- 编写完整的单元测试
- 添加必要的注释说明

### 2. 提交规范
- feat: 新功能
- fix: 修复bug
- docs: 文档更新
- style: 代码格式
- refactor: 重构
- test: 测试
- chore: 构建过程或辅助工具的变动

### 3. 分支管理
- master: 主分支
- develop: 开发分支
- feature/*: 功能分支
- hotfix/*: 紧急修复分支

## 注意事项
1. 遵循模块化开发原则
2. 保持代码简洁清晰
3. 注重代码复用性
4. 完善异常处理
5. 添加必要的日志

## 最佳实践
1. 使用统一的响应格式
2. 规范化异常处理
3. 合理使用加密功能
4. 遵循配置规范
5. 注重代码质量

## 贡献指南
1. Fork 项目
2. 创建特性分支
3. 提交更改
4. 推送到分支
5. 创建 Pull Request

## 许可证
[许可证类型]
