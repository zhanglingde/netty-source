package com.ling.advance.chat.server.service;

/**
 * @author zhangling 2021/5/20 15:34
 */
public abstract class UserServiceFactory {

    private static UserService userService = new UserServiceMemoryImpl();

    public static UserService getUserService() {
        return userService;
    }
}
