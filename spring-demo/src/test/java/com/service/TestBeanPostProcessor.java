package com.service;

import com.spring.annotation.Component;
import com.spring.annotation.EnableAspectJAutoProxy;
import com.spring.processor.BeanPostProcessor;

import java.lang.reflect.Proxy;

@Component
public class TestBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
//        System.out.println("初始化后");

        if (bean.getClass().isAnnotationPresent(EnableAspectJAutoProxy.class)) {
            Object proxyObject = Proxy.newProxyInstance(TestBeanPostProcessor.class.getClassLoader(), bean.getClass().getInterfaces(), (proxy, method, args) -> {
                System.out.println("\r\n代理逻辑\r\n");
                return method.invoke(bean, args);
            });
            // 返回代理对象
            return proxyObject;
        }
        return bean;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
//        System.out.println("初始化前");
        if ("userService".equals(beanName)) {
            ((UserServiceImpl) bean).setName("hello Spring Test");
        }
        return bean;
    }
}
