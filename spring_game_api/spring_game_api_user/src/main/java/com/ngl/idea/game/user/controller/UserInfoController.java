package com.ngl.idea.game.user.controller;

import com.ngl.idea.game.base.dto.UserDTO;
import com.ngl.idea.game.common.core.model.response.ApiResponse;
import com.ngl.idea.game.core.controller.GameBaseController;
import com.ngl.idea.game.user.dto.UserAvatarDTO;
import com.ngl.idea.game.user.service.UserInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.web.bind.annotation.*;

/**
 * 用户信息控制器
 */
@Tag(name = "用户信息接口")
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserInfoController extends GameBaseController {

    private final UserInfoService userInfoService;

    @Operation(summary = "获取用户信息")
    @PostMapping("/getUserInfo")
    public ApiResponse<UserDTO> fetchUserInfo() {
        return ApiResponse.success(userInfoService.getUserInfo(getUserId()));
    }

    @Operation(summary = "上传用户头像")
    @PostMapping("/uploadAvatar")
    public ApiResponse<UserAvatarDTO> uploadAvatar(
            @Parameter(description = "Base64编码的头像数据", required = true) @RequestParam String base64) {
        return ApiResponse.success(userInfoService.uploadAvatar(getUserId(), base64));
    }

    @Operation(summary = "获取用户当前头像")
    @PostMapping("/getCurrentAvatar")
    public ApiResponse<UserAvatarDTO> getCurrentAvatar() {
        return ApiResponse.success(userInfoService.getCurrentAvatar(getUserId()));
    }

    @Operation(summary = "获取用户指定版本的头像")
    @PostMapping("/getAvatarByVersion")
    public ApiResponse<UserAvatarDTO> getAvatarByVersion(
            @Parameter(description = "头像版本", required = true) @RequestParam String version) {
        return ApiResponse.success(userInfoService.getAvatarByVersion(getUserId(), version));
    }

    @Operation(summary = "获取用户头像列表")
    @PostMapping("/getAvatarList")
    public ApiResponse<List<UserAvatarDTO>> getAvatarList() {
        return ApiResponse.success(userInfoService.getAvatarList(getUserId()));
    }
}
