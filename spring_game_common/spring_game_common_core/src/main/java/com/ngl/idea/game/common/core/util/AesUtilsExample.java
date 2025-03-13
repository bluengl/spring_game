package com.ngl.idea.game.common.core.util;

/**
 * AES工具类使用示例
 * 以下代码仅用于演示，实际应用中不建议将密钥硬编码在源代码中
 */
public class AesUtilsExample {

    public static void main(String[] args) {
        try {
            // 示例1: 生成随机密钥并使用GCM模式加解密
            String key = AesUtils.generateKey();
            System.out.println("生成的AES密钥: " + key);
            
            String originalData = "这是需要加密的敏感数据，如密码123456";
            
            // 使用GCM模式加密
            String encryptedGCM = AesUtils.encryptGCM(originalData, key);
            System.out.println("GCM加密结果: " + encryptedGCM);
            
            // 使用GCM模式解密
            String decryptedGCM = AesUtils.decryptGCM(encryptedGCM, key);
            System.out.println("GCM解密结果: " + decryptedGCM);
            
            // 示例2: 使用CBC模式加解密 (兼容性更好)
            String encryptedCBC = AesUtils.encryptCBC(originalData, key);
            System.out.println("CBC加密结果: " + encryptedCBC);
            
            String decryptedCBC = AesUtils.decryptCBC(encryptedCBC, key);
            System.out.println("CBC解密结果: " + decryptedCBC);
            
            // 示例3: 使用简化方法 (默认使用GCM模式)
            String encrypted = AesUtils.encrypt(originalData, key);
            String decrypted = AesUtils.decrypt(encrypted, key);
            System.out.println("简化方法解密结果: " + decrypted);
            
            // 示例4: 使用密码生成密钥
            String password = "user_password";
            String salt = "application_specific_salt_value_12345"; // 至少16字节
            String derivedKey = AesUtils.generateKeyFromPassword(password, salt);
            System.out.println("从密码生成的密钥: " + derivedKey);
            
            String encryptedWithDerivedKey = AesUtils.encrypt(originalData, derivedKey);
            String decryptedWithDerivedKey = AesUtils.decrypt(encryptedWithDerivedKey, derivedKey);
            System.out.println("使用密码派生密钥解密结果: " + decryptedWithDerivedKey);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
} 