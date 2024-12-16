package dev.ngb.issues_logging_app.application.service.impl;

import dev.ngb.issues_logging_app.application.constant.TokenType;
import dev.ngb.issues_logging_app.application.service.TokenService;
import dev.ngb.issues_logging_app.infrastructure.cache.redis.RedisCacheService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TokenServiceImpl implements TokenService {

    private final RedisCacheService redisCacheService;

    public TokenServiceImpl(RedisCacheService redisCacheService) {
        this.redisCacheService = redisCacheService;
    }

    @Override
    public String generateAndSaveUserToken(String userId, TokenType type) {
        String token = generateToken();
        saveToken(userId, token, type);
        return token;
    }

    @Override
    public UUID getUserIdByToken(String token, TokenType type) {
        String key = getKeyCache(token, type);
        String userId = redisCacheService.get(key, String.class)
                .orElseThrow(() -> new IllegalArgumentException("Token is invalid"));
        return UUID.fromString(userId);
    }

    @Override
    public void revokeToken(String token, TokenType type) {
        String key = getKeyCache(token, type);
        redisCacheService.invalidate(key);
    }

    private String getKeyCache(String token, TokenType type) {
        return type.getCacheKeyPattern().formatted(token);
    }

    private void saveToken(String userId, String token, TokenType type) {
        String key = getKeyCache(token, type);
        redisCacheService.save(key, userId);
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }
}
