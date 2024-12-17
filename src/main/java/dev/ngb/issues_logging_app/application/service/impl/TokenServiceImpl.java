package dev.ngb.issues_logging_app.application.service.impl;

import dev.ngb.issues_logging_app.application.constant.TokenType;
import dev.ngb.issues_logging_app.application.service.TokenService;
import dev.ngb.issues_logging_app.infrastructure.cache.CacheFactory;
import dev.ngb.issues_logging_app.infrastructure.cache.redis.RedisCacheService;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class TokenServiceImpl implements TokenService {

    private final CacheFactory cacheFactory;

    public TokenServiceImpl(CacheFactory cacheFactory) {
        this.cacheFactory = cacheFactory;
    }

    @Override
    public String generateAndSaveUserToken(String userId, TokenType type) {
        String token = generateTokenValue();
        saveToken(userId, token, type);
        return token;
    }

    @Override
    public UUID getUserIdByToken(String token, TokenType type) {
        String key = getKeyCache(token, type);
        Cache cache = getTokenCache(type);
        String userId = Optional.ofNullable(cache.get(key, String.class))
                .orElseThrow(() -> new IllegalArgumentException("Token is invalid"));
        return UUID.fromString(userId);
    }

    @Override
    public void revokeToken(String token, TokenType type) {
        String key = getKeyCache(token, type);
        Cache cache = getTokenCache(type);
        cache.evict(key);
    }

    private String getKeyCache(String token, TokenType type) {
        return type.getCacheKeyPattern().formatted(token);
    }

    private void saveToken(String userId, String token, TokenType type) {
        String key = getKeyCache(token, type);
        Cache cache = getTokenCache(type);
        cache.put(key, userId);
    }

    private String generateTokenValue() {
        return UUID.randomUUID().toString();
    }

    private Cache getTokenCache(TokenType type) {
        return cacheFactory.getCache(type.getCacheName());
    }
}
