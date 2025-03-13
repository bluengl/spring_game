package com.ngl.idea.game.common.core.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * AES对称加密工具类
 * 适用于密码等敏感数据的加解密
 */
@Slf4j
@Component
public class AesUtils {

    /**
     * 默认AES加密算法
     */
    private static final String ALGORITHM = "AES";

    /**
     * 推荐的AES工作模式和填充方式 (GCM模式提供了认证功能)
     */
    private static final String TRANSFORMATION_GCM = "AES/GCM/NoPadding";

    /**
     * 兼容模式 (CBC模式兼容性更好，但安全性略低于GCM)
     */
    private static final String TRANSFORMATION_CBC = "AES/CBC/PKCS5Padding";

    /**
     * GCM认证标签长度 (128位)
     */
    private static final int GCM_TAG_LENGTH = 128;

    /**
     * 默认密钥长度 (256位)
     */
    private static final int KEY_SIZE = 256;

    /**
     * 初始化向量长度 (16字节 = 128位)
     */
    private static final int IV_LENGTH = 16;

    /**
     * 生成AES密钥
     *
     * @return Base64编码的AES密钥
     */
    public static String generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        keyGenerator.init(KEY_SIZE, new SecureRandom());
        SecretKey secretKey = keyGenerator.generateKey();
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    /**
     * 生成随机初始化向量
     *
     * @return 初始化向量字节数组
     */
    public static byte[] generateIv() {
        byte[] iv = new byte[IV_LENGTH];
        new SecureRandom().nextBytes(iv);
        return iv;
    }

    /**
     * 使用AES-GCM模式加密数据
     *
     * @param data 待加密数据
     * @param key Base64编码的AES密钥
     * @return Base64编码的加密结果 (IV + 密文)
     */
    public static String encryptGCM(String data, String key) throws Exception {
        // 解码密钥
        byte[] keyBytes = Base64.getDecoder().decode(key);
        SecretKey secretKey = new SecretKeySpec(keyBytes, ALGORITHM);

        // 生成随机IV
        byte[] iv = generateIv();

        // 初始化加密器
        Cipher cipher = Cipher.getInstance(TRANSFORMATION_GCM);
        GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);

        // 加密数据
        byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));

        // 组合IV和密文
        byte[] result = new byte[iv.length + encryptedBytes.length];
        System.arraycopy(iv, 0, result, 0, iv.length);
        System.arraycopy(encryptedBytes, 0, result, iv.length, encryptedBytes.length);

        // 返回Base64编码结果
        return Base64.getEncoder().encodeToString(result);
    }

    /**
     * 使用AES-GCM模式解密数据
     *
     * @param encryptedData Base64编码的加密数据 (IV + 密文)
     * @param key Base64编码的AES密钥
     * @return 解密后的明文
     */
    public static String decryptGCM(String encryptedData, String key) throws Exception {
        // 解码密钥
        byte[] keyBytes = Base64.getDecoder().decode(key);
        SecretKey secretKey = new SecretKeySpec(keyBytes, ALGORITHM);

        // 解码加密数据
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedData);

        // 提取IV和密文
        byte[] iv = new byte[IV_LENGTH];
        byte[] ciphertext = new byte[encryptedBytes.length - IV_LENGTH];
        System.arraycopy(encryptedBytes, 0, iv, 0, IV_LENGTH);
        System.arraycopy(encryptedBytes, IV_LENGTH, ciphertext, 0, ciphertext.length);

        // 初始化解密器
        Cipher cipher = Cipher.getInstance(TRANSFORMATION_GCM);
        GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);

        // 解密数据
        byte[] decryptedBytes = cipher.doFinal(ciphertext);

        // 返回解密后的明文
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    /**
     * 使用AES-CBC模式加密数据 (兼容性更好)
     *
     * @param data 待加密数据
     * @param key Base64编码的AES密钥
     * @return Base64编码的加密结果 (IV + 密文)
     */
    public static String encryptCBC(String data, String key) throws Exception {
        // 解码密钥
        byte[] keyBytes = Base64.getDecoder().decode(key);
        SecretKey secretKey = new SecretKeySpec(keyBytes, ALGORITHM);

        // 生成随机IV
        byte[] iv = generateIv();

        // 初始化加密器
        Cipher cipher = Cipher.getInstance(TRANSFORMATION_CBC);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);

        // 加密数据
        byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));

        // 组合IV和密文
        byte[] result = new byte[iv.length + encryptedBytes.length];
        System.arraycopy(iv, 0, result, 0, iv.length);
        System.arraycopy(encryptedBytes, 0, result, iv.length, encryptedBytes.length);

        // 返回Base64编码结果
        return Base64.getEncoder().encodeToString(result);
    }

    /**
     * 使用AES-CBC模式解密数据 (兼容性更好)
     *
     * @param encryptedData Base64编码的加密数据 (IV + 密文)
     * @param key Base64编码的AES密钥
     * @return 解密后的明文
     */
    public static String decryptCBC(String encryptedData, String key) throws Exception {
        // 解码密钥
        byte[] keyBytes = Base64.getDecoder().decode(key);
        SecretKey secretKey = new SecretKeySpec(keyBytes, ALGORITHM);

        // 解码加密数据
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedData);

        // 提取IV和密文
        byte[] iv = new byte[IV_LENGTH];
        byte[] ciphertext = new byte[encryptedBytes.length - IV_LENGTH];
        System.arraycopy(encryptedBytes, 0, iv, 0, IV_LENGTH);
        System.arraycopy(encryptedBytes, IV_LENGTH, ciphertext, 0, ciphertext.length);

        // 初始化解密器
        Cipher cipher = Cipher.getInstance(TRANSFORMATION_CBC);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);

        // 解密数据
        byte[] decryptedBytes = cipher.doFinal(ciphertext);

        // 返回解密后的明文
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    /**
     * 简化版加密方法 (默认使用GCM模式)
     *
     * @param data 待加密数据
     * @param key Base64编码的AES密钥
     * @return Base64编码的加密结果
     */
    public static String encrypt(String data, String key) throws Exception {
        return encryptCBC(data, key);
    }

    /**
     * 简化版解密方法 (默认使用GCM模式)
     *
     * @param encryptedData Base64编码的加密数据
     * @param key Base64编码的AES密钥
     * @return 解密后的明文
     */
    public static String decrypt(String encryptedData, String key) throws Exception {
        return decryptCBC(encryptedData, key);
    }

    /**
     * 使用密码生成AES密钥 (适用于基于密码的加密场景)
     *
     * @param password 密码
     * @param salt 盐值 (至少16字节)
     * @return Base64编码的AES密钥
     */
    public static String generateKeyFromPassword(String password, String salt) throws Exception {
        byte[] saltBytes = salt.getBytes(StandardCharsets.UTF_8);

        // 使用PBKDF2算法从密码生成密钥 (可替换为更复杂的密钥派生函数)
        javax.crypto.SecretKeyFactory factory = javax.crypto.SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        javax.crypto.spec.PBEKeySpec spec = new javax.crypto.spec.PBEKeySpec(
                password.toCharArray(), saltBytes, 65536, KEY_SIZE);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKey secretKey = new SecretKeySpec(tmp.getEncoded(), ALGORITHM);

        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    /**
     * 解密前端JavaScript加密的数据 (密文格式：salt:iv:ciphertext，均为Base64编码)
     *
     * @param encryptedData 加密数据，格式为 salt:iv:ciphertext
     * @param key 原始密钥字符串
     * @return 解密后的明文
     * @throws Exception 解密过程中可能出现的异常
     */
    public static String decryptFromJS(String encryptedData, String key) throws Exception {
        try {
            log.debug("开始解密JavaScript加密的数据: {}", encryptedData);
            
            // 分割密文
            String[] parts = encryptedData.split(":");
            if (parts.length != 3) {
                throw new IllegalArgumentException("无效的加密数据格式，预期为: salt:iv:ciphertext");
            }
            
            // 解析各部分
            String salt = parts[0];
            String iv = parts[1];
            String ciphertext = parts[2];
            
            log.debug("解析的密文组件 - salt: {}, iv长度: {}, 密文长度: {}", salt, iv.length(), ciphertext.length());
            
            // 解码盐值、IV和密文
            byte[] saltBytes = Base64.getDecoder().decode(salt);
            byte[] ivBytes = Base64.getDecoder().decode(iv);
            byte[] ciphertextBytes = Base64.getDecoder().decode(ciphertext);
            
            // 从密码和盐值派生密钥 (与前端JS实现匹配)
            SecretKey derivedKey;
            try {
                // 使用与JavaScript一致的密钥派生参数
                javax.crypto.SecretKeyFactory factory = javax.crypto.SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
                javax.crypto.spec.PBEKeySpec spec = new javax.crypto.spec.PBEKeySpec(
                        key.toCharArray(), saltBytes, 1000, 256); // 1000次迭代，256位密钥
                
                SecretKey tmp = factory.generateSecret(spec);
                derivedKey = new SecretKeySpec(tmp.getEncoded(), ALGORITHM);
                
                log.debug("成功派生密钥");
            } catch (Exception e) {
                log.warn("密钥派生失败，尝试使用原始密钥: {}", e.getMessage());
                
                // 如果密钥派生失败，尝试使用原始密钥
                byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
                byte[] fixedKeyBytes = new byte[32]; // AES-256需要32字节密钥
                System.arraycopy(keyBytes, 0, fixedKeyBytes, 0, Math.min(keyBytes.length, 32));
                derivedKey = new SecretKeySpec(fixedKeyBytes, ALGORITHM);
            }
            
            // 使用CBC模式解密，确保与JavaScript使用的算法匹配
            Cipher cipher = Cipher.getInstance(TRANSFORMATION_CBC);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);
            cipher.init(Cipher.DECRYPT_MODE, derivedKey, ivParameterSpec);
            
            byte[] decryptedBytes = cipher.doFinal(ciphertextBytes);
            String result = new String(decryptedBytes, StandardCharsets.UTF_8);
            
            log.debug("解密成功，明文长度: {}", result.length());
            return result;
        } catch (Exception e) {
            log.error("解密失败: {}", e.getMessage(), e);
            throw new Exception("解密失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 加密数据为前端JS可解密的格式 (密文格式：salt:iv:ciphertext，均为Base64编码)
     *
     * @param data 待加密数据
     * @param key 原始密钥字符串
     * @return 加密后的数据
     * @throws Exception 加密过程中可能出现的异常
     */
    public static String encryptForJS(String data, String key) throws Exception {
        try {
            log.debug("开始加密数据，长度: {}", data.length());
            
            // 生成随机盐值
            byte[] saltBytes = new byte[16];
            new SecureRandom().nextBytes(saltBytes);
            
            // 生成随机IV
            byte[] ivBytes = generateIv();
            
            // 从密码和盐值派生密钥 (与前端JS实现匹配)
            SecretKey derivedKey;
            try {
                // 使用与JavaScript一致的密钥派生参数
                javax.crypto.SecretKeyFactory factory = javax.crypto.SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
                javax.crypto.spec.PBEKeySpec spec = new javax.crypto.spec.PBEKeySpec(
                        key.toCharArray(), saltBytes, 1000, 256); // 1000次迭代，256位密钥
                
                SecretKey tmp = factory.generateSecret(spec);
                derivedKey = new SecretKeySpec(tmp.getEncoded(), ALGORITHM);
                
                log.debug("成功派生密钥");
            } catch (Exception e) {
                log.warn("密钥派生失败，尝试使用原始密钥: {}", e.getMessage());
                
                // 如果密钥派生失败，尝试使用原始密钥
                byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
                byte[] fixedKeyBytes = new byte[32]; // AES-256需要32字节密钥
                System.arraycopy(keyBytes, 0, fixedKeyBytes, 0, Math.min(keyBytes.length, 32));
                derivedKey = new SecretKeySpec(fixedKeyBytes, ALGORITHM);
            }
            
            // 使用CBC模式加密，确保与JavaScript使用的算法匹配
            Cipher cipher = Cipher.getInstance(TRANSFORMATION_CBC);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(ivBytes);
            cipher.init(Cipher.ENCRYPT_MODE, derivedKey, ivParameterSpec);
            
            byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            
            // 将盐值、IV和密文编码为Base64
            String saltBase64 = Base64.getEncoder().encodeToString(saltBytes);
            String ivBase64 = Base64.getEncoder().encodeToString(ivBytes);
            String ciphertextBase64 = Base64.getEncoder().encodeToString(encryptedBytes);
            
            // 组合为最终密文格式
            String result = String.format("%s:%s:%s", saltBase64, ivBase64, ciphertextBase64);
            
            log.debug("加密成功，密文长度: {}", result.length());
            return result;
        } catch (Exception e) {
            log.error("加密失败: {}", e.getMessage(), e);
            throw new Exception("加密失败: " + e.getMessage(), e);
        }
    }

    /**
     * 从前端解密简单XOR加密的数据 (格式：salt:iv:ciphertext，均为Base64编码)
     * 此方法对应前端的SimpleEncryption实现
     *
     * @param encryptedData 加密数据，格式为 salt:iv:ciphertext
     * @param key 原始密钥字符串
     * @return 解密后的明文
     * @throws Exception 解密过程中可能出现的异常
     */
    public static String decryptSimpleJs(String encryptedData, String key) throws Exception {
        try {
            log.debug("开始解密简单XOR加密的数据: {}", encryptedData);
            
            // 分割密文
            String[] parts = encryptedData.split(":");
            if (parts.length != 3) {
                throw new IllegalArgumentException("无效的加密数据格式，预期为: salt:iv:ciphertext");
            }
            
            // 解析各部分
            String salt = parts[0];
            String iv = parts[1];
            String ciphertext = parts[2];
            
            log.debug("解析的密文组件 - salt: {}, iv长度: {}, 密文长度: {}", salt, iv.length(), ciphertext.length());
            
            // 解码Base64
            byte[] saltBytes = Base64.getDecoder().decode(salt);
            byte[] ivBytes = Base64.getDecoder().decode(iv);
            byte[] ciphertextBytes = Base64.getDecoder().decode(ciphertext);
            
            // 将密钥转换为字节
            byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
            
            // 使用简单的XOR解密
            byte[] plaintextBytes = new byte[ciphertextBytes.length];
            for (int i = 0; i < ciphertextBytes.length; i++) {
                // 使用密钥、盐值和IV混合创建XOR掩码
                byte keyByte = keyBytes[i % keyBytes.length];
                byte saltByte = saltBytes[i % saltBytes.length];
                byte ivByte = ivBytes[i % ivBytes.length];
                
                // 混合掩码
                byte mask = (byte) ((keyByte ^ saltByte ^ ivByte) % 256);
                
                // 解密当前字节
                plaintextBytes[i] = (byte) (ciphertextBytes[i] ^ mask);
            }
            
            // 转换为字符串
            String result = new String(plaintextBytes, StandardCharsets.UTF_8);
            
            log.debug("简单XOR解密成功，明文长度: {}", result.length());
            return result;
        } catch (Exception e) {
            log.error("简单XOR解密失败: {}", e.getMessage(), e);
            throw new Exception("解密失败: " + e.getMessage(), e);
        }
    }
}
