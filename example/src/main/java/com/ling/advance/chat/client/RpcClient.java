package com.ling.advance.chat.client;


import com.ling.advance.chat.message.RpcRequestMessage;
import com.ling.advance.chat.protocol.MessageCodecSharable;
import com.ling.advance.chat.protocol.ProtocolFrameDecoder;
import com.ling.advance.chat.protocol.SequenceIdGenerator;
import com.ling.advance.chat.server.handler.RpcResponseMessageHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * Rpc 客户端
 */
@Slf4j
public class RpcClient {
    public static void main(String[] args) {
        NioEventLoopGroup group = new NioEventLoopGroup();
        LoggingHandler LOGGING_HANDLER = new LoggingHandler();
        MessageCodecSharable MESSAGE_CODEC = new MessageCodecSharable();

        RpcResponseMessageHandler RPC_HANDLER = new RpcResponseMessageHandler();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new ProtocolFrameDecoder());
                            ch.pipeline().addLast(LOGGING_HANDLER);
                            ch.pipeline().addLast(MESSAGE_CODEC);
                            ch.pipeline().addLast(RPC_HANDLER);
                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect("localhost", 8080).sync();
            // 客户端发送 Rpc 消息
            ChannelFuture future = channelFuture.channel().writeAndFlush(new RpcRequestMessage(SequenceIdGenerator.nextId(),
                    "com.ling.netty2.advance.chat.server.service.HelloService",
                    "sayHello",
                    String.class,
                    new Class[]{String.class},
                    new Object[]{"haha"}
            )).addListener(promise -> {
                if (promise.isSuccess()) {
                    log.info("success");
                } else {
                    Throwable cause = promise.cause();
                    log.error("error:{}", cause);
                }
            });
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("客户端错误：{}", e);
        } finally {
            group.shutdownGracefully();
        }
    }
}
