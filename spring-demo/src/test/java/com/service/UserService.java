package com.service;

import com.spring.annotation.Autowired;
import com.spring.annotation.Component;
import com.spring.aware.BeanNameAware;
import com.spring.aware.InitializingBean;

@Component("userService")
//@Scope("prototype")
public class UserService implements BeanNameAware, InitializingBean {
    @Autowired
    private OrderService orderService;

    private String name;

    private String beanName;

    public void setName(String name) {
        this.name = name;
    }

    public void test() {
        System.out.println("UserService class " + orderService);
        System.out.println("UserService BeanName value is " + this.beanName);
        System.out.println("name value is " + name);
    }

    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("afterPropertiesSet Function ");
    }
}
