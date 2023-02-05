package com.spring;

import com.spring.annotation.Component;
import com.spring.annotation.ComponentScan;

import java.io.File;
import java.net.URL;

/**
 * Spring中SpringApplicationContext容器的实现
 */
public class MySpringApplicationContext {
    private Class configClass;
    private static final String CLAZZ_PATH_PREFIX = "com";
    private static final String CLAZZ_PATH_SUFFIX = ".class";

    public MySpringApplicationContext(Class configClass) throws ClassNotFoundException {
        this.configClass = configClass;
        // 解析配置类
        // 获取ComponentScan注解
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

                    }
                }

            }
        }
    }

    public Object getBean(String beanName) {
        return null;
    }
}
