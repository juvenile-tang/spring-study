package com.spring;

import com.spring.annotation.Component;
import com.spring.annotation.ComponentScan;
import com.spring.annotation.Scope;
import com.spring.entity.BeanDefinition;

import java.io.File;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Spring中SpringApplicationContext容器的实现
 */
public class MySpringApplicationContext {
    private Class configClass;
    /** 单例池 */
    private static final ConcurrentHashMap<String, Object> singletonObjects = new ConcurrentHashMap<>();
    /** 系统中所有的BeanDefinition对象都存储在这个Map中 */
    private static final ConcurrentHashMap<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    private static final String CLAZZ_PATH_PREFIX = "com";
    private static final String CLAZZ_PATH_SUFFIX = ".class";
    private static final String BEAN_TYPE_SINGLETO = "singleton";

    public MySpringApplicationContext(Class configClass) throws ClassNotFoundException {
        this.configClass = configClass;
        // 解析配置类
        // 获取ComponentScan注解
        scan(configClass);
    }

    /**
     * 对Spring中的Bean进行扫描
     * @param configClass
     * @throws ClassNotFoundException
     */
    private void scan(Class configClass) throws ClassNotFoundException {
        ComponentScan componentScanAnnotation = (ComponentScan) configClass.getDeclaredAnnotation(ComponentScan.class);
        String scanPath = componentScanAnnotation.value();
        scanPath = scanPath.replaceAll("\\.", "\\/");

        // 扫描
        ClassLoader classLoader = MySpringApplicationContext.class.getClassLoader();
        URL resource = classLoader.getResource(scanPath);
        File file = new File(resource.getFile());
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File classFile : files) {
                String classPath = classFile.getAbsolutePath();

                if (classPath.endsWith(CLAZZ_PATH_SUFFIX)) {
                    classPath = classPath.substring(file.getName().indexOf(CLAZZ_PATH_PREFIX),
                            file.getName().indexOf(CLAZZ_PATH_SUFFIX));
                    classPath = classPath.replaceAll("\\", ".");
                    Class<?> clazz = classLoader.loadClass(classPath);

                    // 判断是否有Component注解
                    if (clazz.isAnnotationPresent(Component.class)) {
                        // 解析当前bean是否是单例bean
                        // 先获取Component注解
                        Component componentAnnotation = clazz.getDeclaredAnnotation(Component.class);
                        String beanName = componentAnnotation.value();

                        // 判断是否有Scope注解
                        BeanDefinition beanDefinition = new BeanDefinition();
                        if (clazz.isAnnotationPresent(Scope.class)) {
                            Scope scopeAnnotation = clazz.getDeclaredAnnotation(Scope.class);
                            beanDefinition.setScope(scopeAnnotation.value());
                        } else {
                            beanDefinition.setScope(BEAN_TYPE_SINGLETO);
                        }
                        beanDefinitionMap.put(beanName, beanDefinition);
                    }
                }

            }
        }
    }

    /**
     * 获取Bean对象
     * @param beanName
     * @return
     */
    public Object getBean(String beanName) {
        if (beanDefinitionMap.containsKey(beanName)) {
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            if (beanDefinition.getScope().equals(BEAN_TYPE_SINGLETO)) {
                Object object = singletonObjects.get(beanName);
                return object;
            } else {
                // 原型的Bean, 创建Bean对象
            }
        } else {
            throw new NullPointerException();
        }
        return null;
    }
}
