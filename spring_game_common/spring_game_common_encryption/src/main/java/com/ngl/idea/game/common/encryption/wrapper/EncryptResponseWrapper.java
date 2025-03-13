package com.ngl.idea.game.common.encryption.wrapper;

import com.ngl.idea.game.common.core.util.Sm2Utils;
import org.springframework.util.StringUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.*;
import java.security.PublicKey;

public class EncryptResponseWrapper extends HttpServletResponseWrapper {
    private ByteArrayOutputStream buffer;
    private ServletOutputStream out;
    private PrintWriter writer;
    private final PublicKey publicKey;

    public EncryptResponseWrapper(HttpServletResponse response, PublicKey publicKey) {
        super(response);
        buffer = new ByteArrayOutputStream();
        this.publicKey = publicKey;
    }

    @Override
    public ServletOutputStream getOutputStream() {
        if (writer != null) {
            throw new IllegalStateException("getWriter() has already been called on this response.");
        }

        if (out == null) {
            out = new ServletOutputStream() {
                @Override
                public void write(int b) {
                    buffer.write(b);
                }

                @Override
                public boolean isReady() {
                    return true;
                }

                @Override
                public void setWriteListener(WriteListener writeListener) {
                    // Not implemented
                }
            };
        }

        return out;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if (out != null) {
            throw new IllegalStateException("getOutputStream() has already been called on this response.");
        }

        if (writer == null) {
            writer = new PrintWriter(new OutputStreamWriter(buffer, getCharacterEncoding()));
        }

        return writer;
    }

    @Override
    public void flushBuffer() throws IOException {
        if (writer != null) {
            writer.flush();
        } else if (out != null) {
            out.flush();
        }
    }

    public byte[] getContent() throws IOException {
        flushBuffer();
        return buffer.toByteArray();
    }

    public void writeContent() throws IOException {
        try {
            byte[] responseBytes = getContent();
            String responseStr = new String(responseBytes, getCharacterEncoding());
            
            if (StringUtils.hasText(responseStr)) {
                // 使用SM2加密
                String encrypted = Sm2Utils.encrypt(publicKey, responseStr);
                byte[] encryptedBytes = encrypted.getBytes(getCharacterEncoding());
                
                getResponse().setContentLength(encryptedBytes.length);
                getResponse().getOutputStream().write(encryptedBytes);
                getResponse().getOutputStream().flush();
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to encrypt response", e);
        }
    }
} 