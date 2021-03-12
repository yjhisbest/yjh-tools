package com.yjh.tools.core.cors;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启跨域拦截器
 *
 * @author yjh
 * */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import({CorsFilterRegistrar.class })
public @interface EnableCorsFilter {

    /**
     * Access-Control-Allow-Origin
     */
    String origin() default "*";

    /**
     * Access-Control-Allow-Origin
     */
    String methods() default "POST, GET, PATCH, DELETE, PUT, OPTIONS";

    /**
     * Access-Control-Allow-Headers
     * 比如: Origin, X-Requested-With, Content-Type, Accept
     */
    String headers();
}
