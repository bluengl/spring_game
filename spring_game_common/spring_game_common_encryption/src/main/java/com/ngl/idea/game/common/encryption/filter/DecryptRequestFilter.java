package com.ngl.idea.game.common.encryption.filter;

import com.ngl.idea.game.common.config.manager.ConfigManager;
import com.ngl.idea.game.common.config.properties.EncryptionProperties;
import com.ngl.idea.game.common.core.util.ServiceLocator;
import com.ngl.idea.game.common.core.util.Sm2Utils;
import com.ngl.idea.game.common.encryption.wrapper.DecryptRequestWrapper;
import com.ngl.idea.game.common.encryption.wrapper.EncryptResponseWrapper;
import lombok.SneakyThrows;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public class DecryptRequestFilter extends OncePerRequestFilter {

    private final AntPathMatcher pathMatcher;

    public DecryptRequestFilter() {
        this.pathMatcher = new AntPathMatcher();
    }

    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        ConfigManager configManager = (ConfigManager) ServiceLocator.getService("configManager");
        // 检查是否需要加解密
        assert configManager != null;
        if (!configManager.getEncryptionConfig().getEnabled() || shouldIgnore(request, configManager.getEncryptionConfig())) {
            filterChain.doFilter(request, response);
            return;
        }
        KeyPair keyPair = Sm2Utils.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        // 创建请求包装器进行解密
        DecryptRequestWrapper requestWrapper = new DecryptRequestWrapper(request, privateKey);

        // 创建响应包装器进行加密
        EncryptResponseWrapper responseWrapper = new EncryptResponseWrapper(response, publicKey);

        try {
            // 执行过滤链
            filterChain.doFilter(requestWrapper, responseWrapper);
            // 写入加密后的响应
            responseWrapper.writeContent();
        } catch (Exception e) {
            throw new ServletException("Error processing encrypted request/response", e);
        }
    }

    private boolean shouldIgnore(HttpServletRequest request, EncryptionProperties encryptionProperties) {
        String requestUri = request.getRequestURI();
        return encryptionProperties.getIgnore() != null &&
                encryptionProperties.getIgnore().getUrls().stream()
                        .anyMatch(pattern -> pathMatcher.match(pattern, requestUri));
    }
}
