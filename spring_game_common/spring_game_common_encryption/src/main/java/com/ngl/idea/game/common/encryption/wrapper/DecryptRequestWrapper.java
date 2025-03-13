package com.ngl.idea.game.common.encryption.wrapper;

import com.ngl.idea.game.common.core.util.Sm2Utils;
import org.apache.commons.io.IOUtils;
import org.springframework.util.StringUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;

public class DecryptRequestWrapper extends HttpServletRequestWrapper {
    private final byte[] body;
    private final PrivateKey privateKey;

    public DecryptRequestWrapper(HttpServletRequest request, PrivateKey privateKey) throws IOException {
        super(request);
        this.privateKey = privateKey;
        // 读取请求体
        String bodyStr = IOUtils.toString(request.getInputStream(), StandardCharsets.UTF_8);
        if (StringUtils.hasText(bodyStr)) {
            try {
                // 使用SM2解密
                String decrypted = Sm2Utils.decrypt(privateKey, bodyStr);
                this.body = decrypted.getBytes(StandardCharsets.UTF_8);
            } catch (Exception e) {
                throw new RuntimeException("Failed to decrypt request body", e);
            }
        } else {
            this.body = new byte[0];
        }
    }

    @Override
    public ServletInputStream getInputStream() {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body);
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
                // Not implemented
            }

            @Override
            public int read() {
                return byteArrayInputStream.read();
            }
        };
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }
}
