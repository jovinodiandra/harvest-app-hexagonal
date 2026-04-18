package org.harvest.application.port.outbound;

import java.time.Duration;

public interface CacheManager {
    <T> void put(String key, T value);
    <T> void put(String key, T value, Duration ttl);
    <T> T get(String key, Class<T> type);
    void evict(String key);
    void evictByPattern(String pattern);
    boolean exists(String key);
}
