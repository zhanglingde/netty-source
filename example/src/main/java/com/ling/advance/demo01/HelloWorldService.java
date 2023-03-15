package com.ling.advance.demo01;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhangling 2021/5/18 15:53
 */
public class HelloWorldService {
    private static final Logger logger = LoggerFactory.getLogger(HelloWorldService.class);

    void start() {
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(boss, worker);
            serverBootstrap.channel(NioServerSocketChannel.class);
            // 修改 系统的接收缓冲区（滑动窗口）
            serverBootstrap.option(ChannelOption.SO_RCVBUF, 10);
            // 修改 netty 的接收缓冲区（ByteBuf）
            // serverBootstrap.childOption(ChannelOption.RCVBUF_ALLOCATOR, new AdaptiveRecvByteBufAllocator(16, 16, 16));
            serverBootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel ch) throws Exception {
                    // 打印接收到的客户端消息
                    // ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
                    ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                    ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {

                        @Override
                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                            logger.debug("连接成功： {}", ctx.channel());
                            super.channelActive(ctx);
                        }

                        @Override
                        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                            logger.debug("断开连接： {}", ctx.channel());
                            super.channelInactive(ctx);
                        }
                    });
                }
            });
            ChannelFuture channelFuture = serverBootstrap.bind(8080);
            logger.debug("{} 绑定端口...", channelFuture.channel());
            channelFuture.sync();
            logger.debug("{} 绑定端口成功", channelFuture.channel());
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            logger.error("server error:{}", e);
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
            logger.info("stoped");
        }
    }

    public static void main(String[] args) {
        new HelloWorldService().start();
    }
}
