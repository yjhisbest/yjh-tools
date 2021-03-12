package com.yjh.tools.core.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

@Slf4j
public class CacheRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        log.info("registry custom cache");
        registry.registerBeanDefinition("customRedisCacheConfiguration", BeanDefinitionBuilder.rootBeanDefinition(CustomRedisCacheConfiguration.class).getBeanDefinition());
        registry.registerBeanDefinition("cacheConfig", BeanDefinitionBuilder.rootBeanDefinition(CacheConfig.class).getBeanDefinition());
    }

}
