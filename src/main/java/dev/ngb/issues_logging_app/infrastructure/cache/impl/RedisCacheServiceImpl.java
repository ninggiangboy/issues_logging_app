package dev.ngb.issues_logging_app.infrastructure.cache.impl;

import dev.ngb.issues_logging_app.common.util.StringUtils;
import dev.ngb.issues_logging_app.infrastructure.cache.RedisCacheService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
public class RedisCacheServiceImpl implements RedisCacheService {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisCacheServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void save(String key, Object value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException("Key and value must not be null");
        }
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void save(String key, Object value, long timeout) {
        if (StringUtils.isBlank(key) || value == null) {
            throw new IllegalArgumentException("Key and value must not be null");
        }
        if (timeout <= 0) {
            throw new IllegalArgumentException("Timeout must be greater than 0");
        }
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.MILLISECONDS);
    }

    @Override
    public <T> Optional<T> get(String key, Class<T> clazz) {
        if (StringUtils.isBlank(key) || clazz == null) {
            throw new IllegalArgumentException("Key and class type must not be null");
        }
        return Optional.ofNullable(redisTemplate.opsForValue().get(key))
                .filter(clazz::isInstance)
                .map(clazz::cast);
    }

    @Override
    public void invalidate(String key) {
        if (StringUtils.isBlank(key)) {
            throw new IllegalArgumentException("Key must not be null");
        }
        redisTemplate.delete(key);
    }

    @Override
    public boolean hasKey(String key) {
        if (StringUtils.isBlank(key)) {
            throw new IllegalArgumentException("Key must not be null");
        }
        return redisTemplate.hasKey(key);
    }
}
