package com.yjh.tools.core.redislimit;

import java.lang.annotation.*;

/**
 * redis流控注解
 *
 * @author yjh
 * */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface RedisLimit {

    /**
     * 限制类型
     */
    RedisLimitType redisLimitType() default RedisLimitType.CUSTOMER;

    /**
     * redis限流的key
     * spel表达式
     * 当RedisLimitType为RedisLimitType.CUSTOMER，必须设置
     */
    String key() default "";

    /**
     * 请求头Header
     * 当RedisLimitType为RedisLimitType.CUSTOMER，必须设置
     */
    String[] headers() default {};

    /**
     * 时间范围，单位秒
     */
    int timeScope() default 3;

    /**
     * 请求限制数
     * 在timeScope时间范围中，允许访问的次数
     * 比如：6，即在timeScope时间范围中，允许访问6次
     */
    long limitTimes() default 6;

    /**
     * 锁定时间，单位秒
     * 等于0，不会锁定。大于0，即设置为锁定时间
     * 比如：
     * a，0, 触发限流，等timeScope的生命周期结束，即可以重新访问
     * b，60，即触发限流，会锁定60秒不可操作
     *
     */
    int lockTime() default 0;

    /**
     * 自定义错误信息
     * */
    String errorMsg() default "操作太频繁，请稍后重试";

}
