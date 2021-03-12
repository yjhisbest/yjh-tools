package com.yjh.tools.core.cache;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Data
@Configuration
@PropertySource("classpath:tools-core-cache.properties")
@ConfigurationProperties(prefix = "tools.cache.redis")
public class RedisCacheConf implements Serializable {
    private static final long serialVersionUID = -1L;

    private String cachePrefix;

    private List<CacheRedisItem> items;

    public int getItemSize() {
        return Optional.of(items).map(t -> t.size()).orElse(0);
    }

    @Data
    @ToString
    public static class CacheRedisItem implements Serializable {

        private static final long serialVersionUID = -1L;

        /**
         * 缓存名称
         */
        private String name;

        /**
         * 缓存时间（秒）
         */
        private int ttlSecond;

        /**
         * 是否缓存 null 值
         */
        private boolean cacheNullValues = true;

    }

}
