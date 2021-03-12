package com.yjh.tools.core.redislimit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

@Slf4j
public class RedisLimitRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        log.info("registry RedisLimit");
        registry.registerBeanDefinition("redisLimitAspect", BeanDefinitionBuilder.rootBeanDefinition(RedisLimitAspect.class).getBeanDefinition());
    }
}
