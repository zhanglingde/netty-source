package com.ling.advance.chat.server.session;

import io.netty.channel.Channel;

/**
 * 会话管理接口（单聊）
 */
public interface Session {

    /**
     * 绑定会话
     * @param channel session 和 channel 绑定
     * @param username session 绑定用户
     */
    void bind(Channel channel, String username);

    /**
     * 解绑会话
     * @param channel channel 和 session 解绑
     */
    void unbind(Channel channel);

    /**
     * 获取属性
     * @param channel
     * @param name 属性名
     * @return 属性值
     */
    Object getAttribute(Channel channel, String name);

    /**
     * 设置属性
     * @param channel
     * @param name
     * @param value
     */
    void setAttribute(Channel channel, String name, Object value);

    /**
     * 根据用户名获取 channel
     * @param username 绑定 session 的用户名
     * @return channel
     */
    Channel getChannel(String username);
}
