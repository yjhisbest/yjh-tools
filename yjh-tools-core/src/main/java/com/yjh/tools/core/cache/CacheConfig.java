package com.yjh.tools.core.cache;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheManager;

@EnableCaching
public class CacheConfig {

    @Bean(name = "cacheManager")
    @Primary
    public CacheManager cacheManager(RedisCacheManager redisCacheManager) {
        return redisCacheManager;
    }

}
