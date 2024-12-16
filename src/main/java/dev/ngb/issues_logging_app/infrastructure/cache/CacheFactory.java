package dev.ngb.issues_logging_app.infrastructure.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class CacheFactory {

    private final Collection<Cache> caches;

    public CacheFactory(
            List<CacheManager> cacheManagers
    ) {
        this.caches = cacheManagers.stream()
                .flatMap(cacheManager -> cacheManager.getCacheNames()
                        .stream()
                        .map(cacheManager::getCache))
                .toList();
        log.info("Initialized CacheFactory with caches: {}", caches.stream().map(Cache::getName).toList());
    }

    public Cache getCache(String cacheName) {
        Optional<Cache> cache = caches.stream()
                .filter(c -> c.getName().equals(cacheName))
                .findFirst();
        if (cache.isEmpty()) {
            throw new IllegalArgumentException("Cache with name " + cacheName + " not found");
        }
        return cache.get();
    }
}
