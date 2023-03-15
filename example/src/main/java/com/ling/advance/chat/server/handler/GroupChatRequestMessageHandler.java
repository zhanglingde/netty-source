package com.ling.advance.chat.server.handler;

import com.ling.advance.chat.message.GroupChatRequestMessage;
import com.ling.advance.chat.message.GroupChatResponseMessage;
import com.ling.advance.chat.server.session.GroupSession;
import com.ling.advance.chat.server.session.GroupSessionFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 聊天室群组消息处理器
 */
@Slf4j
@ChannelHandler.Sharable
public class GroupChatRequestMessageHandler extends SimpleChannelInboundHandler<GroupChatRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupChatRequestMessage msg) throws Exception {
        log.info("聊天室处理器接收到消息：{}", msg);
        String groupName = msg.getGroupName();
        GroupSession groupSession = GroupSessionFactory.getGroupSession();
        List<Channel> channels = groupSession.getMembersChannel(groupName);
        if (channels != null) {
            for (Channel channel : channels) {
                channel.writeAndFlush(new GroupChatResponseMessage(msg.getFrom(), msg.getContent()));
            }
        } else {
            ctx.writeAndFlush(new GroupChatResponseMessage(false, "该聊天室不存在！"));
        }
    }
}
