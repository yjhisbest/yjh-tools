package com.yjh.tools.core.snowflake;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启雪花算法工具
 *
 * @author yjh
 * */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import({SnowflakeRegistrar.class })
public @interface EnableSnowflake {

    /**
     * redis对应机器码key
     * @return
     */
    String machineIdKey();
}
