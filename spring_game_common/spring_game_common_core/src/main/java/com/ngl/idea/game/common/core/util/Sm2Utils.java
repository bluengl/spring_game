package com.ngl.idea.game.common.core.util;

import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.X509EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.io.File;

/**
 * SM2 加密工具类
 */
@Component
public class Sm2Utils {
    private static final String ALGORITHM = "EC";
    private static final String CURVE_NAME = "sm2p256v1";
    private static final String PROVIDER_NAME = "BC";

    static {
        if (Security.getProvider(PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    /**
     * 生成密钥对
     *
     * @return KeyPair 密钥对
     */
    public static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM, PROVIDER_NAME);
        ECGenParameterSpec ecGenParameterSpec = new ECGenParameterSpec(CURVE_NAME);
        keyPairGenerator.initialize(ecGenParameterSpec, new SecureRandom());
        return keyPairGenerator.generateKeyPair();
    }

    /**
     * SM2加密
     *
     * @param publicKey 公钥
     * @param data      明文数据
     * @return 加密后的Base64字符串
     */
    public static String encrypt(PublicKey publicKey, String data) throws Exception {
        SM2Engine engine = new SM2Engine(SM2Engine.Mode.C1C3C2);
        BCECPublicKey bcecPublicKey = (BCECPublicKey) publicKey;
        ECPublicKeyParameters ecPublicKeyParameters = new ECPublicKeyParameters(
                bcecPublicKey.getQ(),
                new org.bouncycastle.crypto.params.ECDomainParameters(
                        bcecPublicKey.getParameters().getCurve(),
                        bcecPublicKey.getParameters().getG(),
                        bcecPublicKey.getParameters().getN()));

        engine.init(true, ecPublicKeyParameters);
        byte[] encrypted = engine.processBlock(data.getBytes(), 0, data.getBytes().length);
        return Base64.getEncoder().encodeToString(encrypted);
    }

    /**
     * SM2解密
     *
     * @param privateKey    私钥
     * @param encryptedData Base64编码的密文
     * @return 解密后的明文
     */
    public static String decrypt(PrivateKey privateKey, String encryptedData) throws Exception {
        SM2Engine engine = new SM2Engine(SM2Engine.Mode.C1C3C2);
        BCECPrivateKey bcecPrivateKey = (BCECPrivateKey) privateKey;
        ECPrivateKeyParameters ecPrivateKeyParameters = new ECPrivateKeyParameters(
                bcecPrivateKey.getD(),
                new org.bouncycastle.crypto.params.ECDomainParameters(
                        bcecPrivateKey.getParameters().getCurve(),
                        bcecPrivateKey.getParameters().getG(),
                        bcecPrivateKey.getParameters().getN()));

        engine.init(false, ecPrivateKeyParameters);
        byte[] decrypted = engine.processBlock(Base64.getDecoder().decode(encryptedData), 0,
                Base64.getDecoder().decode(encryptedData).length);
        return new String(decrypted);
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
     * 读取resource下的SM2公钥
     *
     * @return SM2公钥对象
     * @throws Exception 读取或解析公钥时可能抛出的异常
     */
    public static PublicKey getSm2PublicKey() throws Exception {
        // 使用getResourceAsStream读取resource下的公钥
        try (InputStream inputStream = Sm2Utils.class.getResourceAsStream("/keys/sm2_public_key.pem")) {
            if (inputStream == null) {
                throw new FileNotFoundException("无法找到SM2公钥文件");
            }

            // 读取文件内容
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            String publicKeyPEM = result.toString("UTF-8");

            // 清除PEM格式的头尾和换行符
            publicKeyPEM = publicKeyPEM.replace("-----BEGIN EC PUBLIC KEY-----", "")
                          .replace("-----END EC PUBLIC KEY-----", "")
                          .replaceAll("\\s+", "");

            // 解码Base64获取公钥字节
            byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyPEM);

            // 创建X509EncodedKeySpec对象
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);

            // 获取KeyFactory实例并生成公钥
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM, PROVIDER_NAME);
            return keyFactory.generatePublic(keySpec);
        } catch (IOException | NoSuchAlgorithmException | NoSuchProviderException | java.security.spec.InvalidKeySpecException e) {
            throw new Exception("读取或解析SM2公钥失败: " + e.getMessage());
        }
    }

    /**
     * 读取resource下的SM2私钥
     *
     * @return SM2私钥对象
     * @throws Exception 读取或解析私钥时可能抛出的异常
     */
    public static PrivateKey getSm2PrivateKey() throws Exception {
        // 使用getResourceAsStream读取resource下的私钥
        try (InputStream inputStream = Sm2Utils.class.getResourceAsStream("/keys/sm2_private_key.pem")) {
            if (inputStream == null) {
                throw new FileNotFoundException("无法找到SM2私钥文件");
            }

            // 读取文件内容
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            String privateKeyPEM = result.toString("UTF-8");

            // 清除PEM格式的头尾和换行符
            privateKeyPEM = privateKeyPEM.replace("-----BEGIN EC PRIVATE KEY-----", "")
                           .replace("-----END EC PRIVATE KEY-----", "")
                           .replaceAll("\\s+", "");

            // 解码Base64获取私钥字节
            byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyPEM);

            // 创建PKCS8EncodedKeySpec对象
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);

            // 获取KeyFactory实例并生成私钥
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM, PROVIDER_NAME);
            return keyFactory.generatePrivate(keySpec);
        } catch (IOException | NoSuchAlgorithmException | NoSuchProviderException | java.security.spec.InvalidKeySpecException e) {
            throw new Exception("读取或解析SM2私钥失败: " + e.getMessage());
        }
    }

    /**
     * 生成SM2密钥对并保存到文件
     *
     * @param publicKeyFile 公钥文件路径
     * @param privateKeyFile 私钥文件路径
     * @throws Exception 生成或保存密钥对时可能抛出的异常
     */
    public static void generateAndSaveKeyPair(String publicKeyFile, String privateKeyFile) throws Exception {
        try {
            // 生成SM2密钥对
            KeyPair keyPair = generateKeyPair();
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            // 获取编码后的密钥
            byte[] publicKeyBytes = publicKey.getEncoded();
            byte[] privateKeyBytes = privateKey.getEncoded();

            // 将密钥编码为Base64格式
            String publicKeyPEM = "-----BEGIN EC PUBLIC KEY-----\n" +
                                 Base64.getEncoder().encodeToString(publicKeyBytes) +
                                 "\n-----END EC PUBLIC KEY-----";

            String privateKeyPEM = "-----BEGIN EC PRIVATE KEY-----\n" +
                                  Base64.getEncoder().encodeToString(privateKeyBytes) +
                                  "\n-----END EC PRIVATE KEY-----";

            // 保存公钥到文件
            try (FileOutputStream publicKeyOut = new FileOutputStream(publicKeyFile)) {
                publicKeyOut.write(publicKeyPEM.getBytes(StandardCharsets.UTF_8));
            }

            // 保存私钥到文件
            try (FileOutputStream privateKeyOut = new FileOutputStream(privateKeyFile)) {
                privateKeyOut.write(privateKeyPEM.getBytes(StandardCharsets.UTF_8));
            }
        } catch (Exception e) {
            throw new Exception("生成或保存SM2密钥对失败: " + e.getMessage());
        }
    }

    /**
     * 初始化SM2密钥对
     * 如果密钥文件不存在，则生成新的密钥对并保存
     *
     * @param publicKeyFile 公钥文件路径
     * @param privateKeyFile 私钥文件路径
     * @throws Exception 初始化密钥对时可能抛出的异常
     */
    public static void initSm2KeyPair(String publicKeyFile, String privateKeyFile) throws Exception {
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
