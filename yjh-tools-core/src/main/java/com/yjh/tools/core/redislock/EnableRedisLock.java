package com.yjh.tools.core.redislock;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启@RedisLock注解的分布式锁
 *
 * @author yjh
 * */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import({RedisLockRegistrar.class })
public @interface EnableRedisLock {

    /**
     * 开启watch dog开关：true开启，false关闭
     * 设置为true,RedisLock注解中expires需要为默认值30000才会启用watch dog
     * @return
     */
    boolean openWatchDog() default false;
}
