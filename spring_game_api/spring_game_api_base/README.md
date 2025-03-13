# Spring Game Base API

## 简介
Spring Game Base API 是游戏服务框架的基础API模块，提供用户认证、注册等基础功能。

## 功能特性
- 获取OpenId
- 用户登录
- 用户注册
- 用户退出登录

## API接口说明

### 1. 获取OpenId

**请求URL**: `/api/base/getOpenId`

**请求方式**: POST

**请求参数**:
```json
{
  "userSource": "微信小程序",
  "code": "授权码"
}
```

**响应结果**:
```json
{
  "timestamp": "2025-03-08T07:29:42.330+00:00",
  "status": 200,
  "error": null,
  "path": "/api/base/getOpenId",
  "data": {
    "openId": "openid123456789",
    "userSource": "微信小程序"
  }
}
```

### 2. 用户登录

**请求URL**: `/api/base/login`

**请求方式**: POST

**请求参数**:
```json
{
  "username": "test_user",
  "password": "password123",
  "userSource": "微信小程序"
}
```

**响应结果**:
```json
{
  "timestamp": "2025-03-08T07:29:42.330+00:00",
  "status": 200,
  "error": null,
  "path": "/api/base/login",
  "data": {
    "user": {
      "userId": "10001",
      "username": "test_user",
      "userSource": "微信小程序",
      "openId": "openid123456789",
      "createTime": "2025-03-08T07:29:42.330"
    },
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "tokenType": "Bearer"
  }
}
```

### 3. 用户注册

**请求URL**: `/api/base/register`

**请求方式**: POST

**请求参数**:
```json
{
  "username": "new_user",
  "password": "password123",
  "userSource": "微信小程序",
  "openId": "openid123456789"
}
```

**响应结果**:
```json
{
  "timestamp": "2025-03-08T07:29:42.330+00:00",
  "status": 200,
  "error": null,
  "path": "/api/base/register",
  "data": {
    "userId": "10002",
    "username": "new_user",
    "userSource": "微信小程序",
    "openId": "openid123456789",
    "createTime": "2025-03-08T07:29:42.330"
  }
}
```

### 4. 用户退出登录

**请求URL**: `/api/base/logout`

**请求方式**: POST

**请求参数**: 无

**响应结果**:
```json
{
  "timestamp": "2025-03-08T07:29:42.330+00:00",
  "status": 200,
  "error": null,
  "path": "/api/base/logout",
  "data": null
}
```

## 错误码说明

| 错误码 | 描述 |
| ------ | ---- |
| 1001 | 用户不存在 |
| 1002 | 用户密码错误 |
| 1003 | 用户账号已锁定 |
| 1004 | 用户账号已作废 |
| 1005 | 用户已存在 |
| 1006 | OpenID已绑定其他账号 |
| 2001 | 无效的Token |
| 2002 | Token已过期 |
| 2003 | Token解析失败 |

## 开发环境

- JDK 1.8
- Spring Boot 2.7.18
- MySQL 8.0.28
- MyBatis-Plus 3.5.5

## 快速开始

1. 创建数据库并执行 `db/schema.sql` 脚本
2. 修改 `application-dev.yml` 中的数据库连接信息
3. 启动应用程序
4. 访问 API 接口 