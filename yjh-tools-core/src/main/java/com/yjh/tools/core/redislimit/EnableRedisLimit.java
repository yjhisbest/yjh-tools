package com.yjh.tools.core.redislimit;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启@RedisLimit注解的限流功能
 *
 * @author yjh
 * */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import({RedisLimitRegistrar.class })
public @interface EnableRedisLimit {
}
