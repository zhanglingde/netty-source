package com.ling.basic.demo02;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

/**
 * @author zhangling 2021/5/17 14:25
 */
@Slf4j
public class EventLoopServer {
    public static void main(String[] args) throws InterruptedException {
        new ServerBootstrap()
                /*
                 * boss: 只负责 ServerSocketChannel 上 accept 事件（客户端连接事件），服务器只有一个ServerSocketChannel，所以boss 只会占用一个线程
                 * worker：负责 SocketChannel 上的 读写事件，具体线程数根据业务设置，多路复用，一个线程可以处理多个 Channel IO
                 */
                .group(new NioEventLoopGroup(),new NioEventLoopGroup(2))
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                            /*
                             * 没有添加 编码/解码，所以此处msg 是ByteBuf 类型
                             * 处理读事件
                             */
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf buf = (ByteBuf) msg;
                                log.info(buf.toString(Charset.defaultCharset()));
                            }
                        });
                    }
                })
                .bind(8080).sync();
    }
}
