package com.spring;

import com.spring.annotation.Autowired;
import com.spring.annotation.Component;
import com.spring.annotation.ComponentScan;
import com.spring.annotation.Scope;
import com.spring.aware.BeanNameAware;
import com.spring.aware.InitializingBean;
import com.spring.entity.BeanDefinition;
import lombok.SneakyThrows;
import java.io.File;
import java.lang.reflect.Field;
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

    public MySpringApplicationContext(Class configClass) {
        this.configClass = configClass;
        // 解析配置类
        // 获取ComponentScan注解
        scan(configClass);

        for (String beanName : beanDefinitionMap.keySet()) {
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            // 如果是单例Bean则创建Bean
            if (beanDefinition.getScope().equals(BEAN_TYPE_SINGLETO)) {
                Object bean = createBean(beanName, beanDefinition);
                singletonObjects.put(beanName, bean);
            }
        }
    }

    /**
     * Bean的创建
     * @param beanDefinition
     * @return
     */
    @SneakyThrows
    public Object createBean(String beanName, BeanDefinition beanDefinition) {
        Class clazz = beanDefinition.getClazz();
        // 创建对象
        Object object = clazz.getDeclaredConstructor().newInstance();
        // 依赖注入
        for (Field objectField : clazz.getDeclaredFields()) {
            if (objectField.isAnnotationPresent(Autowired.class)) {

                Autowired autowiredAnnotation = objectField.getAnnotation(Autowired.class);
                if (autowiredAnnotation.required()) {
                    // 根据属性的名称来找对应的属性值
                    Object bean = getBean(objectField.getName());
                    objectField.setAccessible(true);
                    objectField.set(object, bean);
                }

            }
        }

        // Aware回调
        // 如果实现了BeanNameAware接口就调用setBeanName方法
        if (object instanceof BeanNameAware) {
            ((BeanNameAware) object).setBeanName(beanName);
        }

        // 初始化
        if (object instanceof InitializingBean) {
            ((InitializingBean) object).afterPropertiesSet();
        }
        return object;
    }

    /**
     * 对Spring中的Bean进行扫描
     * @param configClass
     */
    @SneakyThrows
    private void scan(Class configClass) {
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
                    classPath = classPath.substring(classPath.indexOf(CLAZZ_PATH_PREFIX),
                            classPath.indexOf(CLAZZ_PATH_SUFFIX));
                    classPath = classPath.replaceAll("\\\\", "\\.");
                    Class<?> clazz = classLoader.loadClass(classPath);

                    // 判断是否有Component注解
                    if (clazz.isAnnotationPresent(Component.class)) {
                        // 解析当前bean是否是单例bean
                        // 先获取Component注解
                        Component componentAnnotation = clazz.getDeclaredAnnotation(Component.class);
                        String beanName = componentAnnotation.value();

                        // 判断是否有Scope注解
                        BeanDefinition beanDefinition = new BeanDefinition();
                        beanDefinition.setClazz(clazz);
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
                return singletonObjects.get(beanName);
            } else {
                // 原型的Bean, 创建Bean对象
                return createBean(beanName, beanDefinition);
            }
        } else {
            throw new NullPointerException();
        }
    }
}
