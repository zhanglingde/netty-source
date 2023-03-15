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
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/**
 * 定长消息解码器 解决 粘包半包问题
 * @author zhangling 2021/5/18 16:02
 */
public class HelloWorldClient3 {
    private static final Logger logger = LoggerFactory.getLogger(HelloWorldClient3.class);

    public static void main(String[] args) {
        NioEventLoopGroup worker = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(worker);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    logger.debug("客户端正在连接...");
                    ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                    ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                        @Override
                        // 客户端连接成功触发该事件
                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                            logger.debug("客户端连接成功");
                            // 发送内容随机的数据包
                            ByteBuf buffer = ctx.alloc().buffer();
                            char c = 'a';
                            Random random = new Random();
                            for (int i = 0; i < 10; i++) {
                                byte[] bytes = new byte[8];
                                // 构建随机长度的字节数组
                                for (int j = 0; j < random.nextInt(8); j++) {
                                    bytes[j] = ((byte) c);
                                }
                                c++;
                                buffer.writeBytes(bytes);
                            }
                            ctx.writeAndFlush(buffer);
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
