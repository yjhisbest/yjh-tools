package com.yjh.tools.core.cache;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启自定义Cache
 * 目前使用redis进行缓存
 * 可自定义缓存的生命周期，在配置文件进行redis-cache.properties进行配置
 * 配置参考例子：tools-core-cache.properties.bak
 *
 * @author yjh
 * */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import({CacheRegistrar.class })
@EnableConfigurationProperties({RedisCacheConf.class })
public @interface EnableCustomCache {
}
