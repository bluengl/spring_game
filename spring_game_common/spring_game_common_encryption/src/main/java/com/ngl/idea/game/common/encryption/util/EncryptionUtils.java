package com.ngl.idea.game.common.encryption.util;

import org.apache.commons.codec.binary.Base64;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import java.io.*;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * 加密工具类
 * 提供非对称加密（RSA）功能
 */
@Component
public class EncryptionUtils {

    private static final String ALGORITHM = "RSA";
    private static final String CHARSET = "UTF-8";

    private static final String PRIVATE_KEY_PATH = "keys/private.key";
    private static final String PUBLIC_KEY_PATH = "keys/public.key";

    private PrivateKey privateKey;
    private PublicKey publicKey;

    @PostConstruct
    public void init() {
        try {
            loadKeys();
        } catch (Exception e) {
            try {
                generateAndSaveKeys();
                loadKeys();
            } catch (Exception ex) {
                throw new RuntimeException("初始化加密工具类失败", ex);
            }
        }
    }

    private void generateAndSaveKeys() throws Exception {
        // 生成密钥对
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        // 获取Base64编码的密钥
        String privateKeyStr = Base64.encodeBase64String(keyPair.getPrivate().getEncoded());
        String publicKeyStr = Base64.encodeBase64String(keyPair.getPublic().getEncoded());

        // 获取资源目录路径
        String resourcePath = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile().getParentFile().getPath() + "/classes";
        File keysDir = new File(resourcePath, "keys");
        if (!keysDir.exists()) {
            keysDir.mkdirs();
        }

        // 保存私钥
        try (FileWriter privateKeyWriter = new FileWriter(new File(keysDir, "private.key"))) {
            privateKeyWriter.write(privateKeyStr);
        }

        // 保存公钥
        try (FileWriter publicKeyWriter = new FileWriter(new File(keysDir, "public.key"))) {
            publicKeyWriter.write(publicKeyStr);
        }
    }

    private void loadKeys() throws Exception {
        // 加载私钥
        String privateKeyContent = loadKeyContent(PRIVATE_KEY_PATH);
        byte[] privateKeyBytes = Base64.decodeBase64(privateKeyContent);
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        this.privateKey = keyFactory.generatePrivate(privateKeySpec);

        // 加载公钥
        String publicKeyContent = loadKeyContent(PUBLIC_KEY_PATH);
        byte[] publicKeyBytes = Base64.decodeBase64(publicKeyContent);
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        this.publicKey = keyFactory.generatePublic(publicKeySpec);
    }

    private String loadKeyContent(String path) throws IOException {
        try (InputStream inputStream = new ClassPathResource(path).getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            return reader.readLine();
        }
    }

    /**
     * 加密请求数据
     * @param content 原始数据
     * @return 加密后的数据
     */
    public String encryptRequest(String content) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, this.publicKey);
        byte[] bytes = cipher.doFinal(content.getBytes(CHARSET));
        return Base64.encodeBase64String(bytes);
    }

    /**
     * 解密请求数据
     * @param content 加密数据
     * @return 解密后的数据
     */
    public String decryptRequest(String content) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, this.privateKey);
        byte[] bytes = cipher.doFinal(Base64.decodeBase64(content));
        return new String(bytes, CHARSET);
    }

    /**
     * 加密响应数据
     * @param content 原始数据
     * @return 加密后的数据
     */
    public String encryptResponse(String content) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, this.privateKey);
        byte[] bytes = cipher.doFinal(content.getBytes(CHARSET));
        return Base64.encodeBase64String(bytes);
    }

    /**
     * 解密响应数据
     * @param content 加密数据
     * @return 解密后的数据
     */
    public String decryptResponse(String content) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, this.publicKey);
        byte[] bytes = cipher.doFinal(Base64.decodeBase64(content));
        return new String(bytes, CHARSET);
    }
}
