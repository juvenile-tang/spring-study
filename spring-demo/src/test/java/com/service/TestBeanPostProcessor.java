package com.service;

import com.spring.annotation.Component;
import com.spring.processor.BeanPostProcessor;

@Component
public class TestBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        System.out.println("初始化后");
        return bean;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        System.out.println("初始化前");
        if ("userService".equals(beanName)) {
            ((UserService) bean).setName("hello Spring Test");
        }
        return bean;
    }
}
