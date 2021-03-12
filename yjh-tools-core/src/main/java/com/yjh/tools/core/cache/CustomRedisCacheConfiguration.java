package com.yjh.tools.core.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 自定义RedisCacheManager
 * 自定义缓存：在配置文件redis-cache.properties配置即可
 *
 * @author yjh
 **/
public class CustomRedisCacheConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomRedisCacheConfiguration.class);

    @Bean(name = "redisCacheManager")
    public RedisCacheManager redisCacheManager(final RedisCacheConf redisCacheConf, RedisConnectionFactory redisConnectionFactory) {
        LOGGER.info("init redis cache, conf=" + redisCacheConf);
        Map<String, RedisCacheConfiguration> confList = new HashMap<>(redisCacheConf.getItemSize() + 1);
        String getCachePrefix = Optional.ofNullable(redisCacheConf.getCachePrefix()).orElse("redisCache");
        final CacheKeyPrefix cacheKeyPrefix = cacheName -> getCachePrefix + ":" + cacheName;

        for (RedisCacheConf.CacheRedisItem item : redisCacheConf.getItems()) {
            confList.put(item.getName(), buildRedisCacheConfiguration(item, cacheKeyPrefix));
        }
        // 默认配置
        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
                // 60s缓存失效
                .entryTtl(Duration.ofSeconds(60))
                // 设置key的序列化方式
                //.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(keySerializer()))
                // 设置value的序列化方式
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(valueSerializer()))
                // 不缓存null值
                .disableCachingNullValues()
                // key前缀
                .computePrefixWith(cacheKeyPrefix);

        RedisCacheManager redisCacheManager = RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(redisConnectionFactory)
                .cacheDefaults(configuration)
                .withInitialCacheConfigurations(confList)
                .build();

        return redisCacheManager;
    }

    /**
     * 给配置的key进行设置
     * */
    private RedisCacheConfiguration buildRedisCacheConfiguration(RedisCacheConf.CacheRedisItem item, CacheKeyPrefix cacheKeyPrefix) {
        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
                // 设置key的序列化方式
               // .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(keySerializer()))
                // 设置value的序列化方式
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(valueSerializer()));
        configuration = configuration.entryTtl(Duration.ofSeconds(item.getTtlSecond()));
        if (!item.isCacheNullValues()) {
            configuration = configuration.disableCachingNullValues();
        }
        // key前缀
        configuration = configuration.computePrefixWith(cacheKeyPrefix);
        return configuration;
    }

    /**
     * key键序列化方式
     * */
    private RedisSerializer<String> keySerializer() {
        return new StringRedisSerializer();
    }

    /**
     * value值序列化方式
     * */
    private RedisSerializer valueSerializer(){
        return new GenericJackson2JsonRedisSerializer();
    }
}
