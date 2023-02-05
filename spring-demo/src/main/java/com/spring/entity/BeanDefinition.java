package com.spring.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Spring中定义Bean的类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BeanDefinition {
    private Class clazz;
    private String scope;
}
