package dev.ngb.issues_logging_app.infrastructure.cache.redis;

import java.util.Optional;

public interface RedisCacheService {
    void save(String key, Object value);

    void save(String key, Object value, long timeout);

    <T> Optional<T> get(String key, Class<T> clazz);

    void invalidate(String key);

    boolean hasKey(String key);
}
