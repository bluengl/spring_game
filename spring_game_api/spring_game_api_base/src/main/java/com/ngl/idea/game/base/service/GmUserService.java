package com.ngl.idea.game.base.service;

import com.ngl.idea.game.base.dto.*;
import com.ngl.idea.game.base.entity.GmUser;

/**
 * 用户服务接口
 */
public interface GmUserService {
    
    /**
     * 获取OpenId
     * @param request 获取OpenId请求
     * @return OpenId响应
     */
    OpenIdResponse getOpenId(OpenIdRequest request);
    
    /**
     * 用户登录
     * @param request 登录请求
     * @return 登录响应
     * @throws Exception 
     */
    LoginResponse login(LoginRequest request) throws Exception;
    
    /**
     * 用户注册
     * @param request 注册请求
     * @return 用户DTO
     */
    UserDTO register(RegisterRequest request);
    
    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户实体
     */
    GmUser findByUsername(String username);
    
    /**
     * 根据OpenId查询用户
     * @param openId OpenId
     * @param userSource 用户来源
     * @return 用户实体
     */
    GmUser findByOpenId(String openId, String userSource);
    
    /**
     * 将用户实体转换为DTO
     * @param user 用户实体
     * @return 用户DTO
     */
    UserDTO convertToDTO(GmUser user);
}
