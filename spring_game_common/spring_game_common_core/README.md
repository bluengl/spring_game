# Spring Game Common Core

## 简介
Spring Game Common Core 是游戏服务框架的核心模块，提供基础功能和通用组件。

## 功能特性
- 通用响应封装
- 全局异常处理
- 通用工具类
- 基础注解
- 常量定义

## 主要组件

### 1. 响应封装
- `ApiResponse`: 统一API响应对象
- `PageResponse`: 分页响应对象
- `ResultCode`: 响应状态码枚举

### 2. 异常处理
- `GlobalExceptionHandler`: 全局异常处理器
- `BusinessException`: 业务异常
- `SystemException`: 系统异常

### 3. 工具类
- `StringUtils`: 字符串工具类
- `DateUtils`: 日期工具类
- `FileUtils`: 文件工具类
- `JsonUtils`: JSON工具类

### 4. 注解
- `Log`: 日志注解
- `Cache`: 缓存注解
- `Validate`: 参数校验注解

### 5. 常量
- `Constants`: 系统常量
- `ErrorCode`: 错误码常量
- `ConfigKey`: 配置键常量

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

### 3. 工具类使用
```java
@Service
public class FileService {
    
    public void saveFile(MultipartFile file) {
        String fileName = FileUtils.generateFileName(file.getOriginalFilename());
        String filePath = FileUtils.saveFile(file, "upload");
        // 处理文件保存逻辑
    }
}
```

### 4. 注解使用
```java
@Service
public class OrderService {
    
    @Log(module = "订单", operation = "创建订单")
    @Cache(key = "order:#{orderId}")
    public Order createOrder(OrderDTO orderDTO) {
        // 创建订单逻辑
    }
}
```

## 注意事项
1. 统一使用ApiResponse封装响应
2. 使用预定义的异常类型
3. 遵循工具类的使用规范
4. 合理使用注解提高代码质量

## 最佳实践
1. 使用统一的响应格式
2. 规范化异常处理
3. 复用工具类功能
4. 合理使用注解
5. 遵循常量命名规范

## 统一响应模板

### 简介
统一响应模板提供了一个标准化的API响应格式，包含以下字段：
- `timestamp`: 当前时间戳，ISO 8601格式
- `status`: HTTP状态码，成功为200，失败为对应错误码
- `error`: 错误信息，成功时为null
- `path`: 请求路径
- `data`: 响应数据，失败时为null

### 响应格式示例

#### 成功响应
```json
{
  "timestamp": "2025-03-08T07:29:42.330+00:00",
  "status": 200,
  "error": null,
  "path": "/api/user/info",
  "data": {
    "userId": 10001,
    "username": "test_user",
    "age": 25
  }
}
```

#### 失败响应
```json
{
  "timestamp": "2025-03-08T07:29:42.330+00:00",
  "status": 500,
  "error": "Internal Server Error",
  "path": "/api/group/postTest",
  "data": null
}
```

### 使用方法

#### 1. 直接使用ApiResponse返回
```java
@RestController
@RequestMapping("/api/user")
public class UserController {
    
    @GetMapping("/info")
    public ApiResponse<UserVO> getUserInfo() {
        UserVO userVO = userService.getUserInfo();
        return ApiResponse.success(userVO);
    }
}
```

#### 2. 直接返回业务数据（会自动包装）
```java
@RestController
@RequestMapping("/api/user")
public class UserController {
    
    @GetMapping("/info")
    public UserVO getUserInfo() {
        return userService.getUserInfo();
    }
}
```

#### 3. 抛出业务异常
```java
@Service
public class UserServiceImpl implements UserService {
    
    @Override
    public UserVO getUserInfo() {
        // 业务逻辑
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        // 正常返回
        return userVO;
    }
}
```

### 注意事项
1. 响应体会在加密前被封装为统一格式
2. String类型的返回值需要特殊处理
3. 如果使用了其他框架（如Swagger）需要确保正确配置 