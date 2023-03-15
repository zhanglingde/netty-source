package com.ling.advance.chat.server.handler;

import com.ling.advance.chat.message.RpcRequestMessage;
import com.ling.advance.chat.message.RpcResponseMessage;
import com.ling.advance.chat.server.service.HelloService;
import com.ling.advance.chat.server.service.ServicesFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * Rpc 请求消息处理器
 */
@Slf4j
@ChannelHandler.Sharable
public class RpcRequestMessageHandler extends SimpleChannelInboundHandler<RpcRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequestMessage msg) {
        RpcResponseMessage response = new RpcResponseMessage();
        response.setSequenceId(msg.getSequenceId());
        try {
            // 根据接口全限定类名 获取接口在内存中的实现
            HelloService service = (HelloService) ServicesFactory.getService(Class.forName(msg.getInterfaceName()));

            Method method = service.getClass().getMethod(msg.getMethodName(), msg.getParameterTypes());
            Object invoke = method.invoke(service, msg.getParameterValue());

            response.setReturnValue(invoke);
        } catch (Exception e) {
            e.printStackTrace();
            String message = e.getCause().getMessage();
            response.setExceptionValue(new Exception("远程调用出错：" + message));
        }
        ctx.writeAndFlush(response);

    }

}
