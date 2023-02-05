package com.spring.processor;

/**
 * Spring Bean的后置处理器,在初始化Bean前或者初始化Bean后
 * 调用,用于和第三方框架进行整合
 */
public interface BeanPostProcessor {
    Object postProcessBeforeInitialization(Object bean, String beanName);

    default Object postProcessAfterInitialization(Object bean, String beanName) {
        return bean;
    }
}
