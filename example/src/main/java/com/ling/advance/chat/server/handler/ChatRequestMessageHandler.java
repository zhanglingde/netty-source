package com.ling.advance.chat.server.handler;

import com.ling.advance.chat.message.ChatRequestMessage;
import com.ling.advance.chat.message.ChatResponseMessage;
import com.ling.advance.chat.server.session.SessionFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 单聊消息处理器
 */
@Slf4j
@ChannelHandler.Sharable
public class ChatRequestMessageHandler extends SimpleChannelInboundHandler<ChatRequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChatRequestMessage msg) throws Exception {
        log.info("msg:{}", msg);
        String to = msg.getTo();
        Channel channel = SessionFactory.getSession().getChannel(to);
        if (channel != null) {
            channel.writeAndFlush(new ChatResponseMessage(msg.getFrom(),msg.getContent()));
        }else{
            ctx.writeAndFlush(new ChatResponseMessage(false,"用户不存在或用户不在线！"));
        }
    }
}
