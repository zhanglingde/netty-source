package com.ling.basic.demo01;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

/**
 * @author zhangling 2021/5/17 10:39
 */
public class HelloServer {
    public static void main(String[] args) {
        // 1. 启动器，负责组装 netty 组件，启动服务器（启动辅助类）
        new ServerBootstrap()
                // 2. BossEventLoop（客户端连接线程池）,WorkerEventLoop(selector,thread)（客户端 IO 事件处理线程池）,group 组
                .group(new NioEventLoopGroup())
                // 3. 选择 服务器的 ServerSocketChannel 实现
                .channel(NioServerSocketChannel.class)   // BIO(OIOServerSocketChannel)
                // 4. boss 负责处理客户端连接，worker（child）负责客户端读写，childHandler决定了 worker（child）能执行哪些操作（handler）
                .childHandler(
                        // 5. channel 代表和客户端进行数据读写的通道 Initializer 初始化，负责添加别的 handler
                        new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        // 6. 添加具体的 handler（处理消息）
                        ch.pipeline().addLast(new StringDecoder()); // 将 ByteBuf 转换为字符串（解码）
                        ch.pipeline().addLast(new SimpleChannelInboundHandler<String>() { // 自定义 handler
                            @Override   // 处理读事件
                            protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
                                System.out.println(msg);
                            }
                        });
                    }
                })
                // 7. 绑定监听端口
                .bind(8080);

    }
}