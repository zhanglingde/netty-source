package com.ling.advance.chat.config;


import com.ling.advance.chat.protocol.Serializer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author zhangling  2021/5/22 11:31
 */
public class Config {
    static Properties properties;

    static {
        try (InputStream is = Config.class.getResourceAsStream("/application.properties")) {
            properties = new Properties();
            properties.load(is);
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static int getServerPort() {
        String value = properties.getProperty("server.prot");
        if (value == null) {
            return 8080;
        }else {
            return Integer.parseInt(value);
        }
    }

    /**
     * 获取配置的序列化算法，默认为jdk序列化方式
     * @return
     */
    public static Serializer.Algorithm getSerializerAlgorithm() {
        String value = properties.getProperty("serializer.algorithm");
        if (value == null) {
            return Serializer.Algorithm.Java;
        }else {
            return Serializer.Algorithm.valueOf(value);
        }
    }
}
