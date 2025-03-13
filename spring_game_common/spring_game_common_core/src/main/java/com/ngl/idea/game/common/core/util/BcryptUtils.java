package com.ngl.idea.game.common.core.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * BCrypt 加密工具类
 */
@Component
public class BcryptUtils {
    private static final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder();

    /**
     * 加密密码
     *
     * @param password 原始密码
     * @return 加密后的密码
     */
    public static String encode(String password) {
        return ENCODER.encode(password);
    }

    /**
     * 验证密码
     *
     * @param rawPassword     原始密码
     * @param encodedPassword 加密后的密码
     * @return 是否匹配
     */
    public static boolean matches(String rawPassword, String encodedPassword) {
        return ENCODER.matches(rawPassword, encodedPassword);
    }
}
