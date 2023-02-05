package com.service;

import com.spring.annotation.Autowired;
import com.spring.annotation.Component;
import com.spring.annotation.Scope;
import com.spring.aware.BeanNameAware;

@Component("userService")
//@Scope("prototype")
public class UserService implements BeanNameAware {
    @Autowired
    private OrderService orderService;

    private String beanName;

    public void test() {
        System.out.println("UserService class " + orderService);
        System.out.println("UserService BeanName value is " + this.beanName);
    }

    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }
}
