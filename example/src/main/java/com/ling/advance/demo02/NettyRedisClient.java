package com.ling.advance.demo02;


import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.nio.charset.Charset;

/**
 * Netty 实现 Redis协议与Redis 服务器通信
 * @author zhangling 2021/5/19 15:32
 */
public class NettyRedisClient {

    private Channel channel;
    final byte[] LINE = "\r\n".getBytes();

    public static void main(String[] args) {
        NettyRedisClient client = new NettyRedisClient();
        client.connect();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        client.set("name","zhang");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        client.get("name");

    }

    /**
     * 连接 Redis 服务器
     */
    private void connect() {
        NioEventLoopGroup worker = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(worker);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                    ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){

                        // 第一次连接成功触发该事件
                        @Override
                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                            ByteBuf buf = ctx.alloc().buffer();
                            byte[] LINE = "\r\n".getBytes();

                            buf.writeBytes("*3".getBytes());
                            buf.writeBytes(LINE);

                            buf.writeBytes("$3".getBytes());
                            buf.writeBytes(LINE);
                            buf.writeBytes("set".getBytes());
                            buf.writeBytes(LINE);

                            buf.writeBytes("$3".getBytes());
                            //buf.writeInt(key.length());
                            buf.writeBytes(LINE);
                            buf.writeBytes("k11".getBytes());
                            buf.writeBytes(LINE);

                            buf.writeBytes("$4".getBytes());
                            //buf.writeInt(value.length());
                            buf.writeBytes(LINE);
                            buf.writeBytes("ling".getBytes());
                            buf.writeBytes(LINE);
                            ctx.writeAndFlush(buf);
                        }
                        // 服务器向客户端发送消息触发该事件
                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                            ByteBuf buffer = (ByteBuf) msg;
                            System.out.println(buffer.toString(Charset.defaultCharset()));
                        }
                    });
                }
            });
            ChannelFuture channelFuture = bootstrap.connect("121.37.185.192", 6379).sync();
            //ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8080).sync();
            channel = channelFuture.channel();
            //channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // set 方法
    public void set(String key, String value) {
        ByteBuf buffer = channel.alloc().buffer();


        buffer.writeBytes("*3".getBytes());
        buffer.writeBytes(LINE);

        buffer.writeBytes("$3".getBytes());
        buffer.writeBytes(LINE);
        buffer.writeBytes("set".getBytes());
        buffer.writeBytes(LINE);

        buffer.writeBytes("$".getBytes());
        buffer.writeBytes((key.length() + "").getBytes());
        buffer.writeBytes(LINE);
        buffer.writeBytes(key.getBytes());
        buffer.writeBytes(LINE);

        buffer.writeBytes("$".getBytes());
        buffer.writeBytes((value.length()+"").getBytes());
        buffer.writeBytes(LINE);
        buffer.writeBytes(value.getBytes());
        buffer.writeBytes(LINE);

        channel.writeAndFlush(buffer);
    }

    // get 方法
    public void get(String key) {
        ByteBuf buffer = channel.alloc().buffer();
        buffer.writeBytes("*2".getBytes());
        buffer.writeBytes(LINE);
        buffer.writeBytes("$3".getBytes());
        buffer.writeBytes(LINE);
        buffer.writeBytes("get".getBytes());
        buffer.writeBytes(LINE);

        buffer.writeBytes(("$" + key.length()).getBytes());
        buffer.writeBytes(LINE);
        buffer.writeBytes(key.getBytes());
        buffer.writeBytes(LINE);
        channel.writeAndFlush(buffer);
    }
}
