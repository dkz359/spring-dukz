package com.spring;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;

/**
 * 自定义Spring容器类
 */
public class DukzApplicationContext {
    private Class configClass;

    public DukzApplicationContext(Class configClass) {
        this.configClass = configClass;

        // 解析配置类
        ComponentScan componentScan = (ComponentScan)configClass.getDeclaredAnnotation(ComponentScan.class);
        String scanPath = componentScan.value();
        String path = scanPath.replace(".", "/");
        System.out.println(path);

        ClassLoader classLoader = DukzApplicationContext.class.getClassLoader();
        URL resource = classLoader.getResource(path);
        File file = new File(resource.getFile());
        if(file.isDirectory()){
            File[] files = file.listFiles();
            for (File f : files) {
                String fileName = f.getAbsolutePath();
                if (fileName.endsWith(".class")) {
                    String startPath = scanPath.replace(".", "\\");
                    String className = fileName.substring(fileName.indexOf(startPath), fileName.indexOf(".class"));
                    className = className.replace("\\", ".");
                    Class<?> clszz ;
                    try {
                        clszz = classLoader.loadClass(className);
                        if (clszz.isAnnotationPresent(Component.class)){
                            System.err.println(clszz.getName());
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }

            }
        }

    }
}
