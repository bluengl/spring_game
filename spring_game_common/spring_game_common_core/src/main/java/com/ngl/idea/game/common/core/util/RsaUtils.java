package com.ngl.idea.game.common.core.util;

import org.springframework.stereotype.Component;

import javax.crypto.Cipher;

import java.io.InputStream;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.File;

/**
 * RSA 加密工具类
 */
@Component
public class RsaUtils {
    private static final String ALGORITHM = "RSA";
    private static final int KEY_SIZE = 2048;
    private static final String CHARSET = "UTF-8";

    /**
     * 生成RSA密钥对
     *
     * @return KeyPair 密钥对
     */
    public static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
        keyPairGenerator.initialize(KEY_SIZE, new SecureRandom());
        return keyPairGenerator.generateKeyPair();
    }

    /**
     * RSA公钥加密
     *
     * @param publicKey 公钥
     * @param data      明文数据
     * @return Base64编码的密文
     */
    public static String encrypt(PublicKey publicKey, String data) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encrypted = cipher.doFinal(data.getBytes(CHARSET));
        return Base64.getEncoder().encodeToString(encrypted);
    }

    /**
     * RSA私钥解密
     *
     * @param privateKey    私钥
     * @param encryptedData Base64编码的密文
     * @return 解密后的明文
     */
    public static String decrypt(PrivateKey privateKey, String encryptedData) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(decrypted, CHARSET);
    }

    /**
     * 将Base64编码的公钥字符串转换为PublicKey对象
     *
     * @param publicKeyBase64 Base64编码的公钥字符串
     * @return PublicKey对象
     */
    public static PublicKey getPublicKey(String publicKeyBase64) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(publicKeyBase64);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * 将Base64编码的私钥字符串转换为PrivateKey对象
     *
     * @param privateKeyBase64 Base64编码的私钥字符串
     * @return PrivateKey对象
     */
    public static PrivateKey getPrivateKey(String privateKeyBase64) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(privateKeyBase64);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * 将公钥转换为Base64字符串
     */
    public static String publicKeyToBase64(PublicKey publicKey) {
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }

    /**
     * 将私钥转换为Base64字符串
     */
    public static String privateKeyToBase64(PrivateKey privateKey) {
        return Base64.getEncoder().encodeToString(privateKey.getEncoded());
    }

    /**
     * 使用私钥对数据进行签名
     *
     * @param privateKey 私钥
     * @param data       待签名数据
     * @return Base64编码的签名
     */
    public static String sign(PrivateKey privateKey, String data) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(data.getBytes(CHARSET));
        return Base64.getEncoder().encodeToString(signature.sign());
    }

    /**
     * 使用公钥验证签名
     *
     * @param publicKey 公钥
     * @param data      原始数据
     * @param sign      Base64编码的签名
     * @return 签名是否有效
     */
    public static boolean verify(PublicKey publicKey, String data, String sign) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(publicKey);
        signature.update(data.getBytes(CHARSET));
        return signature.verify(Base64.getDecoder().decode(sign));
    }

    /**
     * 读取resource下的RSA公钥
     *
     * @return RSA公钥对象
     * @throws Exception 读取或解析公钥时可能抛出的异常
     */
    public static PublicKey getRsaPublicKey() throws Exception {
        // 使用getResourceAsStream读取resource下的公钥
        try (InputStream inputStream = RsaUtils.class.getResourceAsStream("/keys/rsa_public_key.pem")) {
            if (inputStream == null) {
                throw new FileNotFoundException("无法找到RSA公钥文件");
            }

            // 读取文件内容
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            String publicKeyPEM = result.toString(CHARSET);

            // 清除PEM格式的头尾和换行符
            publicKeyPEM = publicKeyPEM.replace("-----BEGIN PUBLIC KEY-----", "")
                            .replace("-----END PUBLIC KEY-----", "")
                            .replaceAll("\\s+", "");

            // 解码Base64获取公钥字节
            byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyPEM);

            // 创建X509EncodedKeySpec对象
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);

            // 获取KeyFactory实例并生成公钥
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            return keyFactory.generatePublic(keySpec);
        } catch (IOException | NoSuchAlgorithmException | java.security.spec.InvalidKeySpecException e) {
            throw new Exception("读取或解析RSA公钥失败: " + e.getMessage());
        }
    }

    /**
     * 读取resource下的RSA私钥
     *
     * @return RSA私钥对象
     * @throws Exception 读取或解析私钥时可能抛出的异常
     */
    public static PrivateKey getRsaPrivateKey() throws Exception {
        // 使用getResourceAsStream读取resource下的私钥
        try (InputStream inputStream = RsaUtils.class.getResourceAsStream("/keys/rsa_private_key.pem")) {
            if (inputStream == null) {
                throw new FileNotFoundException("无法找到RSA私钥文件");
            }

            // 读取文件内容
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            String privateKeyPEM = result.toString(CHARSET);

            // 清除PEM格式的头尾和换行符
            privateKeyPEM = privateKeyPEM.replace("-----BEGIN PRIVATE KEY-----", "")
                           .replace("-----END PRIVATE KEY-----", "")
                           .replaceAll("\\s+", "");

            // 解码Base64获取私钥字节
            byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyPEM);

            // 创建PKCS8EncodedKeySpec对象
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);

            // 获取KeyFactory实例并生成私钥
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            return keyFactory.generatePrivate(keySpec);
        } catch (IOException | NoSuchAlgorithmException | java.security.spec.InvalidKeySpecException e) {
            throw new Exception("读取或解析RSA私钥失败: " + e.getMessage());
        }
    }

    /**
     * 生成RSA密钥对并保存到文件
     *
     * @param publicKeyFile 公钥文件路径
     * @param privateKeyFile 私钥文件路径
     * @throws Exception 生成或保存密钥对时可能抛出的异常
     */
    public static void generateAndSaveKeyPair(String publicKeyFile, String privateKeyFile) throws Exception {
        try {
            // 生成RSA密钥对
            KeyPair keyPair = generateKeyPair();
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            // 获取编码后的密钥
            byte[] publicKeyBytes = publicKey.getEncoded();
            byte[] privateKeyBytes = privateKey.getEncoded();

            // 将密钥编码为Base64格式
            String publicKeyPEM = "-----BEGIN PUBLIC KEY-----\n" +
                                 Base64.getEncoder().encodeToString(publicKeyBytes) +
                                 "\n-----END PUBLIC KEY-----";

            String privateKeyPEM = "-----BEGIN PRIVATE KEY-----\n" +
                                  Base64.getEncoder().encodeToString(privateKeyBytes) +
                                  "\n-----END PRIVATE KEY-----";

            // 保存公钥到文件
            try (FileOutputStream publicKeyOut = new FileOutputStream(publicKeyFile)) {
                publicKeyOut.write(publicKeyPEM.getBytes(CHARSET));
            }

            // 保存私钥到文件
            try (FileOutputStream privateKeyOut = new FileOutputStream(privateKeyFile)) {
                privateKeyOut.write(privateKeyPEM.getBytes(CHARSET));
            }
        } catch (NoSuchAlgorithmException | IOException e) {
            throw new Exception("生成或保存RSA密钥对失败: " + e.getMessage());
        }
    }

    /**
     * 初始化RSA密钥对
     * 如果密钥文件不存在，则生成新的密钥对并保存
     *
     * @param publicKeyFile 公钥文件路径
     * @param privateKeyFile 私钥文件路径
     * @throws Exception 初始化密钥对时可能抛出的异常
     */
    public static void initRsaKeyPair(String publicKeyFile, String privateKeyFile) throws Exception {
        File pubFile = new File(publicKeyFile);
        File priFile = new File(privateKeyFile);

        // 如果公钥或私钥文件不存在，则生成新的密钥对
        if (!pubFile.exists() || !priFile.exists()) {
            // 确保目录存在
            pubFile.getParentFile().mkdirs();
            priFile.getParentFile().mkdirs();

            // 生成并保存密钥对
            generateAndSaveKeyPair(publicKeyFile, privateKeyFile);
        }
    }

}
