package com.ling.advance.demo02;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;

/**
 * http 协议
 * @author zhangling  2021/5/19 20:51
 */
@Slf4j
public class TestHttp {
    public static void main(String[] args) {
        NioEventLoopGroup boos = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(boos, worker);
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                    // http 编码/解码 处理器（入站处理器对请求进行解码，出站处理器对响应进行编码）
                    ch.pipeline().addLast(new HttpServerCodec());
                    // 只处理 HttpRequest 类型的消息处理器，其他类型消息，跳过该处理器
                    ch.pipeline().addLast(new SimpleChannelInboundHandler<HttpRequest>() {
                        @Override
                        protected void channelRead0(ChannelHandlerContext ctx, HttpRequest request) throws Exception {
                            // 获取请求
                            log.debug(request.uri());

                            // 返回响应
                            // 构造方法参数：http版本，响应状态码
                            DefaultFullHttpResponse response = new DefaultFullHttpResponse(request.protocolVersion(),HttpResponseStatus.OK);

                            // 响应头中添加长度，告诉浏览器响应体长度，读取多少长度的内容后浏览器就不在请求
                            byte[] bytes = "<h1>Hello, World</h1>".getBytes();
                            response.headers().setInt(CONTENT_LENGTH, bytes.length);
                            response.content().writeBytes(bytes);
                            ctx.writeAndFlush(response);
                        }
                    });
                    // 对解码 后的结果进行处理
                    // ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                    //     @Override
                    //     public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                    //         // 浏览器访问该服务器，查看接收的消息类型
                    //         log.debug("{}", msg.getClass());
                    //         if (msg instanceof HttpRequest) {
                    //             // 请求行，请求头
                    //         } else if (msg instanceof HttpContent) {
                    //             // 请求体（get请求没有请求体对象中没有内容）
                    //         }
                    //     }
                    // });

                }
            });
            ChannelFuture channelFuture = serverBootstrap.bind(8080).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("server error:{}", e);
        } finally {
            boos.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
