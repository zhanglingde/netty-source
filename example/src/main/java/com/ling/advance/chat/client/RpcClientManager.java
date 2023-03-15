package com.ling.advance.chat.client;


import com.ling.advance.chat.message.RpcRequestMessage;
import com.ling.advance.chat.protocol.MessageCodecSharable;
import com.ling.advance.chat.protocol.ProtocolFrameDecoder;
import com.ling.advance.chat.protocol.SequenceIdGenerator;
import com.ling.advance.chat.server.handler.RpcResponseMessageHandler;
import com.ling.advance.chat.server.service.HelloService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultPromise;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Proxy;

/**
 * Rpc 客户端第二版
 */
@Slf4j
public class RpcClientManager {
    private static Channel channel = null;
    private static final Object LOCK = new Object();

    public static void main(String[] args) {
        // 获取代理对象
        HelloService service = getProxyService(HelloService.class);
        System.out.println(service.sayHello("hello"));
        //System.out.println(service.sayHello("哈哈"));
    }

    /**
     * 获取代理对象
     *
     * @param <T> 被代理的对象的类型
     * @return 返回代理后的对象
     */
    public static <T> T getProxyService(Class<T> serviceClass) {
        ClassLoader loader = serviceClass.getClassLoader();
        Class<?>[] interfaces = new Class[]{serviceClass};
        Object o = Proxy.newProxyInstance(loader, interfaces, (proxy, method, args) -> {
            // 1. 将方法调用转换为 消息对象
            int sequenceId = SequenceIdGenerator.nextId();
            RpcRequestMessage msg = new RpcRequestMessage(
                    sequenceId,
                    serviceClass.getName(),
                    method.getName(),
                    method.getReturnType(),
                    method.getParameterTypes(),
                    args);
            // 2. 将消息对象发送出去
            getChannel().writeAndFlush(msg);

            // 3. 准备一个空 Promise 对象，用来接收结果            指定 promise 对象异步接收结果的线程（eventLoop Nio线程）
            DefaultPromise<Object> promise = new DefaultPromise<>(getChannel().eventLoop());
            RpcResponseMessageHandler.PROMISES.put(sequenceId, promise);

            // 4. 阻塞等待promise 的结果
            promise.await();
            if (promise.isSuccess()) {
                return promise.getNow();
            }else{
                throw new RuntimeException(promise.cause());
            }

        });

        return (T) o;
    }

    /**
     * 单例 获取 channel
     *
     * @return
     */
    public static Channel getChannel() {
        if (channel != null) {
            return channel;
        }
        synchronized (LOCK) {   // 多线程都进入该方法时
            if (channel != null) {
                return channel;
            }
            initChannel();
            return channel;
        }
    }

    /**
     * 初始化 channel
     */
    private static void initChannel() {
        NioEventLoopGroup group = new NioEventLoopGroup();
        LoggingHandler LOGGING_HANDLER = new LoggingHandler();
        MessageCodecSharable MESSAGE_CODEC = new MessageCodecSharable();

        RpcResponseMessageHandler RPC_HANDLER = new RpcResponseMessageHandler();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new ProtocolFrameDecoder());
                ch.pipeline().addLast(LOGGING_HANDLER);
                ch.pipeline().addLast(MESSAGE_CODEC);
                ch.pipeline().addLast(RPC_HANDLER);
            }
        });
        try {
            channel = bootstrap.connect("localhost", 8080).sync().channel();
            channel.closeFuture().addListener(future -> {
                group.shutdownGracefully();
            });
        } catch (Exception e) {
            log.error("客户端错误：{}", e);
        }
    }
}
