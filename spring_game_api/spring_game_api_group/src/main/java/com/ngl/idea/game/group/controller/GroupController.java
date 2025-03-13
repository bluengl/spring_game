package com.ngl.idea.game.group.controller;

import com.ngl.idea.game.common.core.model.TokenUser;
import com.ngl.idea.game.common.core.model.response.ApiResponse;
import com.ngl.idea.game.common.security.filter.TokenUserHttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/group")
public class GroupController {

    @GetMapping("/getTest")
    public ResponseEntity<?> getTest(@RequestParam String openId) {
        System.out.println(openId);
        return ResponseEntity.ok(openId);
    }

    @PostMapping("/postTest")
    public ApiResponse<?> postTest(HttpServletRequest request) {
        TokenUserHttpServletRequest request1 = (TokenUserHttpServletRequest) request;
        TokenUser tokenUser = request1.getTokenUser();
        return ApiResponse.success("121");
    }
}
