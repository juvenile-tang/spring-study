package com.service;

import com.spring.annotation.Autowired;
import com.spring.annotation.Component;
import com.spring.annotation.EnableAspectJAutoProxy;

@Component("userService")
//@Scope("prototype")
@EnableAspectJAutoProxy
public class UserServiceImpl implements UserService {
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

}
