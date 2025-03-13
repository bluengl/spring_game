package com.ngl.idea.game.common.encryption.util;

import org.apache.commons.codec.binary.Base64;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class KeyGenerator {
    private static final String ALGORITHM = "RSA";
    private static final int KEY_SIZE = 2048;

    public static void main(String[] args) {
        try {
            // 生成密钥对
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
            keyPairGenerator.initialize(KEY_SIZE);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            // 获取Base64编码的密钥
            String privateKey = Base64.encodeBase64String(keyPair.getPrivate().getEncoded());
            String publicKey = Base64.encodeBase64String(keyPair.getPublic().getEncoded());

            // 创建密钥目录
            String resourcePath = "spring_game_common/spring_game_common_encryption/src/main/resources/keys";
            File keysDir = new File(resourcePath);
            if (!keysDir.exists()) {
                keysDir.mkdirs();
            }

            // 保存私钥
            try (FileWriter privateKeyWriter = new FileWriter(new File(keysDir, "private.key"))) {
                privateKeyWriter.write(privateKey);
            }

            // 保存公钥
            try (FileWriter publicKeyWriter = new FileWriter(new File(keysDir, "public.key"))) {
                publicKeyWriter.write(publicKey);
            }

            System.out.println("密钥对生成成功！");
            System.out.println("私钥已保存到: " + new File(keysDir, "private.key").getAbsolutePath());
            System.out.println("公钥已保存到: " + new File(keysDir, "public.key").getAbsolutePath());

        } catch (NoSuchAlgorithmException | IOException e) {
            System.err.println("生成密钥对失败：" + e.getMessage());
            e.printStackTrace();
        }
    }
}
