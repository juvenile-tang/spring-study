package com.spring.aware;

public interface InitializingBean {
    void afterPropertiesSet() throws Exception;
}
