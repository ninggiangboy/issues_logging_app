package dev.ngb.issues_logging_app.infrastructure.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import dev.ngb.issues_logging_app.infrastructure.property.TokenExpirationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    private final TokenExpirationProperties tokenExpirationProperties;

    public CacheConfig(TokenExpirationProperties tokenExpirationProperties) {
        this.tokenExpirationProperties = tokenExpirationProperties;
    }

    @Bean
    @Primary
    @CaffeineCache
    public CacheManager dailyCaffeineCacheManager() {
        final int maximumSize = 100;
        final int expirationHour = 24;
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
        Cache<Object, Object> cache = Caffeine.newBuilder()
                .expireAfterWrite(expirationHour, TimeUnit.HOURS)
                .maximumSize(maximumSize)
                .build();
        caffeineCacheManager.registerCustomCache(CacheConstant.TAGS_CACHE_NAME, cache);
        caffeineCacheManager.registerCustomCache(CacheConstant.CATEGORIES_CACHE_NAME, cache);
        return caffeineCacheManager;
    }

    @Bean
    @RedisCache
    public CacheManager redisCacheManager(RedisConnectionFactory connectionFactory) {
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();

        cacheConfigurations.put(CacheConstant.REFRESH_TOKEN_CACHE_NAME,
                RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofMillis(tokenExpirationProperties.refresh())));
        cacheConfigurations.put(CacheConstant.VERIFICATION_TOKEN_CACHE_NAME,
                RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofMillis(tokenExpirationProperties.verification())));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(RedisCacheConfiguration.defaultCacheConfig())
                .withInitialCacheConfigurations(cacheConfigurations)
                .transactionAware()
                .build();
    }
}
