package com.ling.advance.chat.server.session;

/**
 * @author zhangling  2021/5/20 20:47
 */
public abstract class SessionFactory {

    private static Session session = new SessionMemoryImpl();

    public static Session getSession() {
        return session;
    }
}
