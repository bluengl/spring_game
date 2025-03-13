# 加密模块使用说明

## 简介
加密模块提供基于RSA的非对称加密功能，用于对API请求和响应数据进行加密和解密。该模块使用2048位的RSA密钥对，确保数据传输的安全性。

## 功能特性
- RSA非对称加密
- 请求数据加密/解密
- 响应数据加密/解密
- 自动密钥加载
- 异常处理和日志记录

## 使用方法

### 1. 添加依赖
在需要使用加密功能的模块的`pom.xml`中添加依赖：
```xml
<dependency>
    <groupId>com.ngl.idea.game</groupId>
    <artifactId>spring_game_common_encryption</artifactId>
    <version>${project.version}</version>
</dependency>
```

### 2. 注入加密工具类
在需要使用加密功能的类中注入`EncryptionUtils`：
```java
@Autowired
private EncryptionUtils encryptionUtils;
```

### 3. 使用加密功能
```java
// 加密请求数据
String encryptedRequest = encryptionUtils.encryptRequest("原始请求数据");

// 解密请求数据
String decryptedRequest = encryptionUtils.decryptRequest(encryptedRequest);

// 加密响应数据
String encryptedResponse = encryptionUtils.encryptResponse("原始响应数据");

// 解密响应数据
String decryptedResponse = encryptionUtils.decryptResponse(encryptedResponse);
```

## 密钥管理

### 1. 生成密钥对
使用`KeyGenerator`类生成新的密钥对：
```bash
java -cp target/encryption-1.0-SNAPSHOT.jar com.ngl.idea.game.common.encryption.KeyGenerator
```

### 2. 密钥文件位置
- 私钥：`src/main/resources/keys/private.key`
- 公钥：`src/main/resources/keys/public.key`

### 3. 密钥格式
- 密钥以Base64编码格式存储
- 私钥使用PKCS8格式
- 公钥使用X509格式

## 注意事项
1. 请妥善保管私钥文件，不要将其提交到版本控制系统
2. 建议在生产环境中使用更安全的密钥存储方式
3. 定期更换密钥对以提高安全性
4. 加密数据大小不应超过RSA密钥长度限制

## 最佳实践
1. 只对敏感数据进行加密
2. 使用HTTPS传输加密数据
3. 实现密钥轮换机制
4. 监控加密/解密操作的性能
5. 记录加密/解密操作的日志

## 常见问题

### 1. 加密失败
- 检查密钥文件是否存在
- 检查密钥格式是否正确
- 检查数据大小是否超出限制

### 2. 解密失败
- 检查加密数据格式是否正确
- 检查是否使用了正确的密钥
- 检查数据是否被篡改

### 3. 性能问题
- 考虑使用混合加密（RSA+AES）
- 优化加密数据大小
- 使用缓存机制 
