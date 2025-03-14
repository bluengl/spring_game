package com.ngl.idea.game.common.core.util;

import java.util.UUID;

public class UUIDUtils {
    public static String getUUID() {
        return UUIDUtils.getUUID(32);
    }

    public static String getUUID(int length) {
        return UUID.randomUUID().toString().replace("-", "").substring(0, length);
    }   
}
