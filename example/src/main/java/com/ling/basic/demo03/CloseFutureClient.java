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
import java.util.Scanner;

/**
 * @author zhangling 2021/5/17 11:13
 */
@Slf4j
public class CloseFutureClient {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        ChannelFuture channelFuture = new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override   // 在连接建立后被调用
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new StringEncoder());
                    }
                })
                .connect(new InetSocketAddress("localhost", 8080));

        Channel channel = channelFuture.sync().channel();
        log.info("{}", channel);

        // 不断向服务器发送消息
        new Thread(() -> {
            Scanner sc = new Scanner(System.in);
            while (true) {
                String line = sc.nextLine();
                if ("q".equals(line)) {
                    channel.close();
                    //log.debug("处理关闭之后的操作");   // 不能在这里处理关闭后操作，close 也是一个异步方法
                    break;
                }
                channel.writeAndFlush(line);
            }
        }, "input").start();

        // 1. 同步关闭 sync
        //ChannelFuture closeFuture = channel.closeFuture();
        //log.debug("waiting close...");
        //closeFuture.sync();
        //log.info("处理关闭之后的操作...");

        // 2. addListener 异步关闭
        ChannelFuture closeFuture = channel.closeFuture();
        closeFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                log.info("处理关闭之后的操作...");
                group.shutdownGracefully();   // 优雅的关闭客户端
            }
        });

        // 使用 addListener 回调方式 异步处理结果
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            // 在 nio 线程建立连接后，会调用operationComplete方法回调执行结果
            public void operationComplete(ChannelFuture future) throws Exception {
                Channel channel1 = future.channel();
                channel1.writeAndFlush(channel1.id() + " connect success...");
            }
        });

    }
}
