package com.ling.basic.demo03;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @author zhangling 2021/5/17 11:13
 */
@Slf4j
public class ChannelClient {
    public static void main(String[] args) throws InterruptedException {
        ChannelFuture channelFuture = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override   // 在连接建立后被调用
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new StringEncoder());
                    }
                })
                // 异步非阻塞， main 发起调用，真正执行 connect 是 nio 线程
                .connect(new InetSocketAddress("localhost", 8080));

        // 1. 使用 sync 方法同步处理结果（阻塞方法，直到连接建立）
        channelFuture.sync();
        Channel channel = channelFuture.channel();  //代表连接对象（Channel）
        log.info("{}", channel);
        channel.writeAndFlush("hello");

        // 2. 使用 addListener 回调方式 异步处理结果
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            // 在 nio 线程建立连接后，会调用operationComplete方法回调执行结果
            public void operationComplete(ChannelFuture future) throws Exception {
                Channel channel1 = future.channel();
                log.info("{}", channel1);
                channel1.writeAndFlush("hello,world");
            }
        });

    }
}
