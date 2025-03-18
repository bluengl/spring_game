package com.ngl.idea.game.group.controller;

import com.ngl.idea.game.common.core.model.TokenUser;
import com.ngl.idea.game.common.core.model.response.ApiResponse;
import com.ngl.idea.game.core.controller.GameBaseController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/api/group")
public class GroupController extends GameBaseController {

    @GetMapping("/getTest")
    public ResponseEntity<?> getTest(@RequestParam String openId) {
        System.out.println(openId);
        return ResponseEntity.ok(openId);
    }

    @PostMapping("/groupList")
    public ApiResponse<?> groupList() {
        TokenUser userInfo = getUserInfo();
        return ApiResponse.success(Arrays.asList("123", "234", "345"));
    }
}
