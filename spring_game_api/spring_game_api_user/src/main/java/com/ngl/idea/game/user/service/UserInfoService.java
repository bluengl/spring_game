package com.ngl.idea.game.user.service;

import java.util.List;

import com.ngl.idea.game.base.dto.UserDTO;
import com.ngl.idea.game.user.dto.UserAvatarDTO;

/**
 * 用户信息服务接口
 */
public interface UserInfoService {
    
    /**
     * 获取用户信息
     * @param userId 用户ID
     * @return 用户DTO
     */
    UserDTO getUserInfo(String userId);
    
    /**
     * 上传用户头像
     * @param userId 用户ID
     * @param base64 Base64编码的头像数据
     * @return 用户头像DTO
     */
    UserAvatarDTO uploadAvatar(String userId, String base64);
    
    /**
     * 获取用户当前头像
     * @param userId 用户ID
     * @return 用户头像DTO
     */
    UserAvatarDTO getCurrentAvatar(String userId);
    
    /**
     * 根据版本获取用户头像
     * @param userId 用户ID
     * @param version 版本
     * @return 用户头像DTO
     */
    UserAvatarDTO getAvatarByVersion(String userId, String version);
    
    /**
     * 获取用户头像列表
     * @param userId 用户ID
     * @return 用户头像DTO列表
     */
    List<UserAvatarDTO> getAvatarList(String userId);
} 
