package com.yjh.tools.demo;

import com.yjh.tools.core.cache.EnableCustomCache;
import com.yjh.tools.core.cors.EnableCorsFilter;
import com.yjh.tools.core.redislimit.EnableRedisLimit;
import com.yjh.tools.core.redislock.EnableRedisLock;
import com.yjh.tools.core.snowflake.EnableSnowflake;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Slf4j
@SpringBootApplication
@EnableAspectJAutoProxy(exposeProxy = true)
// 开启分布式锁,同事支持watch dog
@EnableRedisLock(openWatchDog = true)
// 开启自定义redis缓存
@EnableCustomCache
// 开启流控
@EnableRedisLimit
// 开启跨域拦截器
@EnableCorsFilter(headers = "Origin, X-Requested-With, Content-Type, Accept, accessToken")
// 开启雪花算法工具
@EnableSnowflake(machineIdKey = "tools:demo:snowflake:key")
public class WebApplication {

    public static void main(String[] args) {
        log.info("================= project start =================");
        SpringApplication.run(WebApplication.class, args);
        log.info("================= project start finish =================");
    }

}
