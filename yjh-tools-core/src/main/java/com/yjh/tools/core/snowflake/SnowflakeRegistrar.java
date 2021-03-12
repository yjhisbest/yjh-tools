package com.yjh.tools.core.snowflake;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 注册雪花算法工具
 *
 * @author yjh
 */
@Slf4j
public class SnowflakeRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry registry) {
        log.info("registry snowflake");
        AnnotationAttributes EnableDbAttributes = AnnotationAttributes.fromMap(annotationMetadata.getAnnotationAttributes(EnableSnowflake.class.getName()));
        String machineIdKey = EnableDbAttributes.getString("machineIdKey");
        MachineIdManager.machineIdKey = machineIdKey;
        registry.registerBeanDefinition("machineIdManager", BeanDefinitionBuilder.rootBeanDefinition(MachineIdManager.class).getBeanDefinition());
    }
}
