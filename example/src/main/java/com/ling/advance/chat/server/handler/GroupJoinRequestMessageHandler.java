package com.ling.advance.chat.server.handler;

import com.ling.advance.chat.message.GroupJoinRequestMessage;
import com.ling.advance.chat.message.GroupJoinResponseMessage;
import com.ling.advance.chat.server.session.Group;
import com.ling.advance.chat.server.session.GroupSessionFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 加入群聊
 * @author zhangling 2021/5/21 9:57
 */
@ChannelHandler.Sharable
public class GroupJoinRequestMessageHandler extends SimpleChannelInboundHandler<GroupJoinRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupJoinRequestMessage msg) throws Exception {
        Group group = GroupSessionFactory.getGroupSession().joinMember(msg.getGroupName(), msg.getUsername());
        if (group != null) {
            ctx.writeAndFlush(new GroupJoinResponseMessage(true, "成功加入" + msg.getGroupName()));
        } else {
            ctx.writeAndFlush(new GroupJoinResponseMessage(false, msg.getGroupName() + "不存在！"));
        }
    }
}
