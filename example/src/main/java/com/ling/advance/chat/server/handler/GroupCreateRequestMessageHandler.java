package com.ling.advance.chat.server.handler;

import com.ling.advance.chat.message.GroupCreateRequestMessage;
import com.ling.advance.chat.message.GroupCreateResponseMessage;
import com.ling.advance.chat.server.session.Group;
import com.ling.advance.chat.server.session.GroupSession;
import com.ling.advance.chat.server.session.GroupSessionFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.List;
import java.util.Set;

/**
 * 创建聊天室 群组
 * @author zhangling  2021/5/20 23:17
 */
@ChannelHandler.Sharable
public class GroupCreateRequestMessageHandler extends SimpleChannelInboundHandler<GroupCreateRequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupCreateRequestMessage msg) throws Exception {
        String groupName = msg.getGroupName();
        Set<String> members = msg.getMembers();
        GroupSession groupSession = GroupSessionFactory.getGroupSession();
        Group group = groupSession.createGroup(groupName, members);

        GroupCreateResponseMessage message;
        if (group == null) {
            message = new GroupCreateResponseMessage(true, groupName + "创建成功！");
            ctx.writeAndFlush(message);
            // 发送拉群消息
            List<Channel> channels = groupSession.getMembersChannel(groupName);
            for (Channel channel : channels) {
                channel.writeAndFlush(new GroupCreateResponseMessage(true, "您已被拉入" + groupName));
            }
        } else {
            message = new GroupCreateResponseMessage(false, groupName + "已经存在！");
            ctx.writeAndFlush(message);
        }

    }
}
