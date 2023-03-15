package com.ling.advance.chat.server.service;

import com.ling.advance.chat.config.Config;

import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务工厂，根据接口查询内存中的接口实现类
 */
public class ServicesFactory {
    static Properties properties;
    static Map<Class<?>, Object> map = new ConcurrentHashMap<>();

    // 配置文件中配置的接口与接口实现放入内存
    static {
        try (InputStream is = Config.class.getResourceAsStream("/application.properties")){
            properties = new Properties();
            properties.load(is);
            Set<String> names = properties.stringPropertyNames();
            for (String name : names) {
                if (name.endsWith("Service")) {
                    Class<?> interfaceClass = Class.forName(name);
                    Class<?> instanceClass = Class.forName(properties.getProperty(name));
                    map.put(interfaceClass, instanceClass.newInstance());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据接口的全限定类名获取接口的实现
     * @param interfaceClass 接口的全限定类名
     * @param <T>
     * @return
     */
    public static <T> T getService(Class<T> interfaceClass) {
        return (T) map.get(interfaceClass);
    }

}
