package com.yjh.tools.core.cors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 注册解决跨域拦截器
 *
 * @author yjh
 */
@Slf4j
public class CorsFilterRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry registry) {
        log.info("registry corsFilter");
        AnnotationAttributes EnableDbAttributes = AnnotationAttributes.fromMap(annotationMetadata.getAnnotationAttributes(EnableCorsFilter.class.getName()));
        String origin = EnableDbAttributes.getString("origin");
        String methods = EnableDbAttributes.getString("methods");
        String headers = EnableDbAttributes.getString("headers");
        log.info("EnableCorsFilter origin:{}, methods:{}, headers:{}", origin, methods, headers);
        CorsFilter.origin = origin;
        CorsFilter.methods = methods;
        CorsFilter.headers = headers;
        registry.registerBeanDefinition("corsFilter", BeanDefinitionBuilder.rootBeanDefinition(CorsFilter.class).getBeanDefinition());
    }
}
