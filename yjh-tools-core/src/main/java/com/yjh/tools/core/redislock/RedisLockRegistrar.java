package com.yjh.tools.core.redislock;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

@Slf4j
public class RedisLockRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry registry) {
        log.info("registry RedisLock");
        AnnotationAttributes EnableDbAttributes = AnnotationAttributes.fromMap(annotationMetadata.getAnnotationAttributes(EnableRedisLock.class.getName()));
        boolean openWatchDog = EnableDbAttributes.getBoolean("openWatchDog");
        RedisLockAspect.openWatchDog = openWatchDog;
        registry.registerBeanDefinition("redisLockAspect", BeanDefinitionBuilder.rootBeanDefinition(RedisLockAspect.class).getBeanDefinition());
    }
}
