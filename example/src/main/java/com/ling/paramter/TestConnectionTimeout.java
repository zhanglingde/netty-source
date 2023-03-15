package com.ling.paramter;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 测试连接超时参数：CONNECT_TIMEOUT_MILLIS
 */
@Slf4j
public class TestConnectionTimeout {

    public static void main(String[] args) {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(group)
                    // 设置客户端连接超时参数
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,1000)
                    .channel(NioSocketChannel.class)
                    .handler(new LoggingHandler());
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8080);
            channelFuture.sync().channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.debug("timeout");
        }finally {
            group.shutdownGracefully();
        }
    }
}
