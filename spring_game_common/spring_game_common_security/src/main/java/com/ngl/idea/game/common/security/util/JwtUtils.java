package com.ngl.idea.game.common.security.util;

import com.ngl.idea.game.common.core.model.TokenUser;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.ngl.idea.game.common.config.manager.ConfigManager;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

@Component
@RequiredArgsConstructor
@RefreshScope
public class JwtUtils {

    private static final PathMatcher pathMatcher = new AntPathMatcher();

    @Resource
    private ConfigManager configManager;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(configManager.getSecurityConfig().getJwt().getSecret().getBytes());
    }

    public String generateToken(TokenUser tokenUser) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", tokenUser.getUserId());
        claims.put("username", tokenUser.getUsername());
        claims.put("userSource", tokenUser.getUserSource());
        return createToken(claims, tokenUser.getUserId(), configManager.getSecurityConfig().getJwt().getExpiration());
    }

    public String generateRefreshToken(TokenUser tokenUser) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", tokenUser.getUserId());
        claims.put("username", tokenUser.getUsername());
        claims.put("userSource", tokenUser.getUserSource());
        return createToken(claims, tokenUser.getUserId(), configManager.getSecurityConfig().getJwt().getRefreshExpiration());
    }

    private String createToken(Map<String, Object> claims, String subject, Long expiration) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public TokenUser extractTokenUser(String token) {
        Claims claims = extractAllClaims(token);
        TokenUser tokenUser = new TokenUser();
        tokenUser.setUserId(claims.get("userId", String.class));
        tokenUser.setUsername(claims.get("username", String.class));
        tokenUser.setUserSource(claims.get("userSource", String.class));
        return tokenUser;
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, java.util.function.Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, TokenUser tokenUser) {
        TokenUser extractedUser = extractTokenUser(token);
        return (extractedUser.getUserId().equals(tokenUser.getUserId()) && !isTokenExpired(token));
    }

    public String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(configManager.getSecurityConfig().getJwt().getHeader());
        if (bearerToken != null && bearerToken.startsWith(configManager.getSecurityConfig().getJwt().getPrefix() + " ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateJwtToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getUserIdFromToken(String token) {
        return extractTokenUser(token).getUserId();
    }

    public boolean isIgnoreRequest(HttpServletRequest request) {
        return configManager.getSecurityConfig().getIgnore().getUrls().stream().anyMatch(ignore -> pathMatcher.match(ignore, request.getRequestURI()));
    }
}
