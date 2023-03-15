package com.ling.advance.demo01;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 短连接 解决 粘包问题
 *
 * @author zhangling 2021/5/18 16:02
 */
public class HelloWorldClient2 {
    private static final Logger logger = LoggerFactory.getLogger(HelloWorldClient2.class);

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            send();
        }
        System.out.println("finished...");
    }

    private static void send() {
        NioEventLoopGroup worker = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(worker);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    logger.debug("客户端正在连接...");
                    ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                        @Override
                        // 客户端连接成功触发该事件
                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                            logger.debug("客户端连接成功");
                            ByteBuf buffer = ctx.alloc().buffer();
                            buffer.writeBytes(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,16,17});
                            ctx.writeAndFlush(buffer);
                            // 发完消息关闭
                            ctx.close();
                        }
                    });
                }
            });
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8080).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            logger.error("客户端出错：{}", e);
        } finally {
            worker.shutdownGracefully();
        }
    }
}
