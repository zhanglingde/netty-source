package com.ling.basic.demo02;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * EventLoopGroup 细分处理长连接任务
 *
 * @author zhangling 2021/5/17 14:25
 */
@Slf4j
public class EventLoopServer2 {
    public static void main(String[] args) throws InterruptedException {
        DefaultEventLoopGroup normalWorkers = new DefaultEventLoopGroup(2);
        new ServerBootstrap()
                /*
                 * boss: 只负责 ServerSocketChannel 上 accept 事件（客户端连接事件），服务器只有一个ServerSocketChannel，所以boss 只会占用一个线程
                 * worker：负责 SocketChannel 上的 读写事件，具体线程数根据业务设置，多路复用，一个线程可以处理多个 Channel IO
                 */
                .group(new NioEventLoopGroup(), new NioEventLoopGroup(2))
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {

                        ch.pipeline().addLast("myHandler1", new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf buf = msg instanceof ByteBuf ? (ByteBuf) msg : null;
                                if (buf != null) {
                                    byte[] bytes = new byte[16];
                                    ByteBuf len = buf.readBytes(bytes, 0, buf.readableBytes());
                                    log.info(new String(bytes));
                                }
                                ctx.fireChannelRead(msg);  // 让消息传递给下一个 handler
                            }
                        }).addLast(normalWorkers, "myHandler2", new ChannelInboundHandlerAdapter() {
                            // 使用另外一个EventLoop 处理
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf buf = msg instanceof ByteBuf ? (ByteBuf) msg : null;
                                if (buf != null) {
                                    byte[] bytes = new byte[16];
                                    ByteBuf len = buf.readBytes(bytes, 0, buf.readableBytes());
                                    log.info(new String(bytes));
                                }
                            }
                        });
                    }
                })
                .bind(8080).sync();
    }
}
