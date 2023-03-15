package com.ling.advance.chat.server.handler;

import com.ling.advance.chat.message.GroupMembersRequestMessage;
import com.ling.advance.chat.message.GroupMembersResponseMessage;
import com.ling.advance.chat.server.session.GroupSessionFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Set;

/**
 * 获取聊天室成员消息 处理器
 */
@ChannelHandler.Sharable
public class GroupMembersRequestHandler extends SimpleChannelInboundHandler<GroupMembersRequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupMembersRequestMessage msg) throws Exception {
        Set<String> members = GroupSessionFactory.getGroupSession().getMembers(msg.getGroupName());
        ctx.writeAndFlush(new GroupMembersResponseMessage(members));

    }
}
