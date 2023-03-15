package com.ling.advance.chat.server.session;

/**
 * @author zhangling  2021/5/20 21:33
 */
public abstract class GroupSessionFactory {

    private static GroupSession groupSession = new GroupSessionMemoryImpl();

    public static GroupSession getGroupSession() {
        return groupSession;
    }
}
