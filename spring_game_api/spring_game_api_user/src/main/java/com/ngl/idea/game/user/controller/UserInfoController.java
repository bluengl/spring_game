package com.ngl.idea.game.user.controller;

import com.alibaba.fastjson.JSON;
import com.ngl.idea.game.base.dto.UserDTO;
import com.ngl.idea.game.common.core.model.response.ApiResponse;
import com.ngl.idea.game.common.security.filter.TokenUserHttpServletRequest;
import com.ngl.idea.game.core.controller.GameBaseController;
import com.ngl.idea.game.user.dto.UserAvatarDTO;
import com.ngl.idea.game.user.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.*;

/**
 * 用户信息控制器
 */
@Api(tags = "用户信息接口")
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserInfoController extends GameBaseController {

    private final UserInfoService userInfoService;

    @ApiOperation("获取用户信息")
    @PostMapping("/getUserInfo")
    public ApiResponse<UserDTO> getUserInfo(HttpServletRequest request) {
        return ApiResponse.success(userInfoService.getUserInfo(((TokenUserHttpServletRequest) request).getTokenUser().getUserId()));
    }

    @ApiOperation("上传用户头像")
    @PostMapping("/uploadAvatar")
    public ApiResponse<UserAvatarDTO> uploadAvatar(
            HttpServletRequest request, 
            @ApiParam(value = "Base64编码的头像数据", required = true)
            @RequestBody String jsonString) {
        UserAvatarDTO userAvatarDTO = JSON.parseObject(jsonString, UserAvatarDTO.class);
        return ApiResponse.success(userInfoService.uploadAvatar(getUserId(), userAvatarDTO.getBase64()));
    }

    @ApiOperation("获取用户当前头像")
    @PostMapping("/getCurrentAvatar")
    public ApiResponse<UserAvatarDTO> getCurrentAvatar(HttpServletRequest request) {
        return ApiResponse.success(userInfoService.getCurrentAvatar(getUserId()));
    }

    @ApiOperation("获取用户指定版本的头像")
    @PostMapping("/getAvatarByVersion")
    public ApiResponse<UserAvatarDTO> getAvatarByVersion(
            HttpServletRequest request,
            @ApiParam(value = "头像版本", required = true)
            @RequestBody String jsonString) {
        UserAvatarDTO userAvatarDTO = JSON.parseObject(jsonString, UserAvatarDTO.class);
        return ApiResponse.success(userInfoService.getAvatarByVersion(getUserId(), userAvatarDTO.getVersion()));
    }

    @ApiOperation("获取用户头像列表")
    @PostMapping("/getAvatarList")
    public ApiResponse<List<UserAvatarDTO>> getAvatarList(HttpServletRequest request) {
        return ApiResponse.success(userInfoService.getAvatarList(getUserId()));
    }
}
