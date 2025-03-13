package com.ngl.idea.game.common.core.controller;

import com.ngl.idea.game.common.core.exception.BusinessException;
import com.ngl.idea.game.common.core.model.response.ApiResponse;
import com.ngl.idea.game.common.core.model.response.ResultCode;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 示例控制器
 * 仅用于演示统一响应模板的使用
 */
@RestController
@RequestMapping("/api/example")
public class ExampleController {

    /**
     * 使用ApiResponse直接返回
     */
    @GetMapping("/response1")
    public ApiResponse<String> getWithApiResponse() {
        return ApiResponse.success("这是直接使用ApiResponse返回的数据");
    }

    /**
     * 直接返回数据（自动包装）
     */
    @GetMapping("/response2")
    public String getWithoutApiResponse() {
        return "这是直接返回的数据，会被自动包装";
    }

    /**
     * 返回对象数据
     */
    @GetMapping("/user")
    public UserVO getUser() {
        return new UserVO(1L, "测试用户", 28);
    }

    /**
     * 返回列表数据
     */
    @GetMapping("/users")
    public List<UserVO> getUsers() {
        return Arrays.asList(
            new UserVO(1L, "用户1", 25),
            new UserVO(2L, "用户2", 30),
            new UserVO(3L, "用户3", 35)
        );
    }

    /**
     * 触发业务异常
     */
    @GetMapping("/error")
    public ApiResponse<Object> getError() {
        throw new BusinessException(ResultCode.USER_NOT_FOUND);
    }

    /**
     * 触发系统异常
     */
    @GetMapping("/exception")
    public ApiResponse<Object> getException() {
        // 除以0触发算术异常
        int result = 10 / 0;
        return ApiResponse.success(result);
    }

    /**
     * 用户对象
     */
    @Data
    static class UserVO {
        private Long id;
        private String name;
        private Integer age;

        public UserVO(Long id, String name, Integer age) {
            this.id = id;
            this.name = name;
            this.age = age;
        }
    }
} 