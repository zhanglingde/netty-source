package com.ling.advance.chat.server.service;

/**
 * @author zhangling 2021/5/24 14:14
 */
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String msg) {
        int i = 1/0;
        return msg;
    }
}
