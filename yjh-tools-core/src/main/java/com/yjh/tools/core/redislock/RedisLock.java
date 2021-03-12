package com.yjh.tools.core.redislock;

import java.lang.annotation.*;

/**
 * redis分布式锁注解
 *
 * @author yjh
 * */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface RedisLock {

    /**
     * redis锁的key
     * spel表达式
     */
    String key() default "";

    /**
     * redis锁的最大持续时间，单位毫秒
     */
    int expires() default RedisLockAspect.LOCK_DEFAULT_EXPIRES;

    /**
     * 等待时间，单位毫秒
     * 默认时间-1，即不等待
     * */
    int waitTime() default -1;

    /**
     * 锁生效条件
     * spel表达式
     */
    String condition() default "true";

    /**
     * 自定义错误信息
     * spel表达式
     * 不填写就告诉用户系统默认错误信息：程序出了个小差，
     * 填写则告诉用户自定义的错误信息
     * */
    String errorMsg() default "";

}
