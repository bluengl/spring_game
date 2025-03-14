package com.ngl.idea.game.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ngl.idea.game.base.dto.UserDTO;
import com.ngl.idea.game.common.core.exception.BusinessException;
import com.ngl.idea.game.common.core.model.response.ResultCode;
import com.ngl.idea.game.common.core.util.UUIDUtils;
import com.ngl.idea.game.core.entity.GmUser;
import com.ngl.idea.game.core.entity.GmUserAvatar;
import com.ngl.idea.game.core.mapper.GmUserMapper;
import com.ngl.idea.game.core.mapper.GmUserAvatarMapper;
import com.ngl.idea.game.user.dto.UserAvatarDTO;
import com.ngl.idea.game.user.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户信息服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserInfoServiceImpl implements UserInfoService {

    private final GmUserMapper userMapper;
    private final GmUserAvatarMapper avatarMapper;

    @Override
    public UserDTO getUserInfo(String userId) {
        GmUser user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        return userDTO;
    }
    

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserInfo(String userId, UserDTO userDTO) {
        GmUser user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        BeanUtils.copyProperties(userDTO, user);
        userMapper.updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserAvatarDTO uploadAvatar(String userId, String base64) {
        // 验证用户是否存在
        GmUser user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        // 查询用户当前头像
        UserAvatarDTO currentAvatar = getCurrentAvatar(userId);
        if (currentAvatar != null) {
            // 更新当前头像状态
            GmUserAvatar updateAvatar = new GmUserAvatar();
            updateAvatar.setAvatarId(currentAvatar.getAvatarId());
            updateAvatar.setActive("0");
            updateAvatar.setUpdateTime(LocalDateTime.now());
            avatarMapper.updateById(updateAvatar);
        }

        // 生成新的头像版本
        String version = UUIDUtils.getUUID();

        // 创建新的头像记录
        GmUserAvatar avatar = new GmUserAvatar();
        avatar.setUserId(userId);
        avatar.setBase64(base64);
        avatar.setVersion(version);
        avatar.setStatus("1"); // 1-有效
        avatar.setActive("1"); // 1-有效
        avatar.setCreateTime(LocalDateTime.now());

        // 保存头像
        avatarMapper.insert(avatar);

        // 转换为DTO
        UserAvatarDTO avatarDTO = new UserAvatarDTO();
        BeanUtils.copyProperties(avatar, avatarDTO);
        return avatarDTO;
    }

    @Override
    public UserAvatarDTO getCurrentAvatar(String userId) {
        // 查询用户最新的有效头像
        LambdaQueryWrapper<GmUserAvatar> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GmUserAvatar::getUserId, userId)
                .eq(GmUserAvatar::getStatus, "1")
                .eq(GmUserAvatar::getActive, "1")
                .orderByDesc(GmUserAvatar::getCreateTime)
                .last("LIMIT 1");

        GmUserAvatar avatar = avatarMapper.selectOne(queryWrapper);
        if (avatar == null) {
            return null;
        }

        UserAvatarDTO avatarDTO = new UserAvatarDTO();
        BeanUtils.copyProperties(avatar, avatarDTO);
        return avatarDTO;
    }

    @Override
    public UserAvatarDTO getAvatarByVersion(String userId, String version) {
        // 查询指定版本的头像
        LambdaQueryWrapper<GmUserAvatar> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GmUserAvatar::getUserId, userId)
                .eq(GmUserAvatar::getVersion, version)
                .eq(GmUserAvatar::getStatus, "1");

        GmUserAvatar avatar = avatarMapper.selectOne(queryWrapper);
        if (avatar == null) {
            return null;
        }

        UserAvatarDTO avatarDTO = new UserAvatarDTO();
        BeanUtils.copyProperties(avatar, avatarDTO);
        return avatarDTO;
    }

    @Override
    public List<UserAvatarDTO> getAvatarList(String userId) {
        // 查询用户所有有效头像
        LambdaQueryWrapper<GmUserAvatar> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GmUserAvatar::getUserId, userId)
                .eq(GmUserAvatar::getStatus, "1")
                .orderByDesc(GmUserAvatar::getCreateTime);

        List<GmUserAvatar> avatarList = avatarMapper.selectList(queryWrapper);
        if (avatarList == null || avatarList.isEmpty()) {
            return Collections.emptyList();
        }

        return avatarList.stream()
                .map(avatar -> {
                    UserAvatarDTO avatarDTO = new UserAvatarDTO();
                    BeanUtils.copyProperties(avatar, avatarDTO);
                    return avatarDTO;
                })
                .collect(Collectors.toList());
    }
} 