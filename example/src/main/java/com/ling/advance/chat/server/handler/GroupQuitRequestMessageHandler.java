package com.ling.advance.chat.server.handler;

import com.ling.advance.chat.message.GroupQuitRequestMessage;
import com.ling.advance.chat.message.GroupQuitResponseMessage;
import com.ling.advance.chat.server.session.Group;
import com.ling.advance.chat.server.session.GroupSessionFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 退出聊天室消息 处理器
 */
@ChannelHandler.Sharable
public class GroupQuitRequestMessageHandler extends SimpleChannelInboundHandler<GroupQuitRequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupQuitRequestMessage msg) throws Exception {
        Group group = GroupSessionFactory.getGroupSession().removeMember(msg.getGroupName(), msg.getUsername());
        if (group != null) {
            ctx.writeAndFlush(new GroupQuitResponseMessage(true, "成功退出" + msg.getGroupName()));
        } else {
            ctx.writeAndFlush(new GroupQuitResponseMessage(false, msg.getGroupName() + "不存在！"));
        }
    }
}
