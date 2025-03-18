package com.ngl.idea.game.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ngl.idea.game.base.dto.UserDTO;
import com.ngl.idea.game.common.core.util.UUIDUtils;
import com.ngl.idea.game.core.entity.GmUser;
import com.ngl.idea.game.core.entity.GmUserAvatar;
import com.ngl.idea.game.core.mapper.GmUserAvatarMapper;
import com.ngl.idea.game.core.mapper.GmUserMapper;
import com.ngl.idea.game.user.dto.UserAvatarDTO;
import com.ngl.idea.game.user.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户信息服务实现类
 */
@Service
@RequiredArgsConstructor
public class UserInfoServiceImpl implements UserInfoService {

    private final GmUserMapper gmUserMapper;
    private final GmUserAvatarMapper gmUserAvatarMapper;

    @Override
    public UserDTO getUserInfo(String userId) {
        GmUser gmUser = gmUserMapper.selectById(userId);
        if (gmUser == null) {
            return null;
        }
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(gmUser, userDTO);
        return userDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserAvatarDTO uploadAvatar(String userId, String base64) {
        // 更新之前的头像为非激活状态
        LambdaUpdateWrapper<GmUserAvatar> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(GmUserAvatar::getUserId, userId)
                .eq(GmUserAvatar::getActive, "1")
                .eq(GmUserAvatar::getStatus, "1")
                .set(GmUserAvatar::getActive, "0")
                .set(GmUserAvatar::getUpdateTime, LocalDateTime.now());
        gmUserAvatarMapper.update(null, updateWrapper);
        // 生成随机头像ID
        String avatarId = UUIDUtils.getUUID();
        // 创建头像记录
        GmUserAvatar avatar = new GmUserAvatar();
        avatar.setAvatarId(avatarId);
        avatar.setUserId(userId);
        avatar.setBase64(base64);
        avatar.setVersion(UUIDUtils.getUUID());
        avatar.setStatus("1");
        avatar.setActive("1");
        avatar.setCreateTime(LocalDateTime.now());
        // 保存头像记录
        gmUserAvatarMapper.insert(avatar);
        // 转换为DTO
        return convertToDTO(avatar);
    }

    @Override
    public UserAvatarDTO getCurrentAvatar(String userId) {
        // 查询最新的头像记录
        LambdaQueryWrapper<GmUserAvatar> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(GmUserAvatar::getUserId, userId)
                .eq(GmUserAvatar::getActive, "1")
                .eq(GmUserAvatar::getStatus, "1")
                .orderByDesc(GmUserAvatar::getCreateTime)
                .last("LIMIT 1");
        GmUserAvatar avatar = gmUserAvatarMapper.selectOne(queryWrapper);
        // 如果没有头像记录，返回null
        if (avatar == null) {
            return null;
        }
        // 转换为DTO
        return convertToDTO(avatar);
    }

    @Override
    public UserAvatarDTO getAvatarByVersion(String userId, String version) {
        // 查询指定版本的头像记录
        LambdaQueryWrapper<GmUserAvatar> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(GmUserAvatar::getUserId, userId)
                .eq(GmUserAvatar::getStatus, "1")
                .eq(GmUserAvatar::getVersion, version);
        GmUserAvatar avatar = gmUserAvatarMapper.selectOne(queryWrapper);
        // 如果没有头像记录，返回null
        if (avatar == null) {
            return null;
        }
        // 转换为DTO
        return convertToDTO(avatar);
    }

    @Override
    public List<UserAvatarDTO> getAvatarList(String userId) {
        // 查询头像记录列表
        LambdaQueryWrapper<GmUserAvatar> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(GmUserAvatar::getUserId, userId)
                .eq(GmUserAvatar::getStatus, "1")
                .orderByDesc(GmUserAvatar::getCreateTime);
        List<GmUserAvatar> avatarList = gmUserAvatarMapper.selectList(queryWrapper);
        // 转换为DTO列表
        List<UserAvatarDTO> dtoList = new ArrayList<>();
        for (GmUserAvatar avatar : avatarList) {
            dtoList.add(convertToDTO(avatar));
        }
        return dtoList;
    }

    /**
     * 将实体转换为DTO
     * @param avatar 头像实体
     * @return 头像DTO
     */
    private UserAvatarDTO convertToDTO(GmUserAvatar avatar) {
        UserAvatarDTO dto = new UserAvatarDTO();
        BeanUtils.copyProperties(avatar, dto);
        return dto;
    }
} 
