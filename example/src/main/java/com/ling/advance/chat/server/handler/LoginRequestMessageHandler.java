package com.ling.advance.chat.server.handler;

import com.ling.advance.chat.message.LoginRequestMessage;
import com.ling.advance.chat.message.LoginResponseMessage;
import com.ling.advance.chat.server.service.UserServiceFactory;
import com.ling.advance.chat.server.session.SessionFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 业务处理器(只接收登录消息)
 */
@Slf4j
@ChannelHandler.Sharable
public class LoginRequestMessageHandler extends SimpleChannelInboundHandler<LoginRequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestMessage msg) throws Exception {
        boolean login = UserServiceFactory.getUserService().login(msg.getUsername(), msg.getPassword());
        LoginResponseMessage message;
        if (login) {
            log.info("用户登录成功：{}", msg.getUsername());
            // 用户登录成功，将用户名 和 Channel 保存到内存中
            SessionFactory.getSession().bind(ctx.channel(), msg.getUsername());
            message = new LoginResponseMessage(true, "登录成功！");
        } else {
            message = new LoginResponseMessage(false, "用户名或密码错误！");
        }
        ctx.writeAndFlush(message);
    }


}
