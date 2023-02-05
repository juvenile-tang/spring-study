package com.service;

import com.spring.annotation.Autowired;
import com.spring.annotation.Component;
import com.spring.annotation.Scope;

@Component("userService")
//@Scope("prototype")
public class UserService {
    @Autowired
    private OrderService orderService;

    public void test() {
        System.out.println("UserService class " + orderService);
    }
}
