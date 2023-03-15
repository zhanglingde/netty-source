package com.ling.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * NIO 单线程模型
 * @author zhangling  2021/5/11 21:55
 */
public class Server {
    public static void main(String[] args) throws IOException {
        // channel通道是全双工模式，写的同时可以读，读的同时可以写
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.socket().bind(new InetSocketAddress("127.0.0.1",8888));

        // socket连接通道设置为非阻塞，默认为阻塞状态
        ssc.configureBlocking(false);

        System.out.println("server started, listening on = " + ssc.getLocalAddress());
        // 构建一个 Selector(大管家)，管理所有客户端连接
        Selector selector = Selector.open();

        /*
         * OP_ACCEPT:客户端连接事件
         * 注册 selector，Selector 监听 OP_ACCEPT 事件
         */
        ssc.register(selector, SelectionKey.OP_ACCEPT);
        while (true) {
            // 每一次循环发现 Selector 管理的所有事件
            // select 阻塞方法，发现Selector管理的所有Channel的事件
            selector.select();
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> it = keys.iterator();
            while (it.hasNext()) {
                SelectionKey key = it.next();
                // 处理事件前先移除，不然下次循环还会处理该事件
                it.remove();
                handle(key);
            }
        }

    }

    /**
     * 处理 Channel 通道 中的事件
     * @param key
     */
    private static void handle(SelectionKey key) {
        if (key.isAcceptable()) {       // 客户端连接事件
            try {
                ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                // 一个客户端连接 使用一个 SocketChannel
                SocketChannel sc = ssc.accept();
                // 设置非阻塞
                sc.configureBlocking(false);
                // 和客户端连接的SocketChannel 注册 Selector，使用Selector管理该Channel，且该Channel监听OP_READ 事件
                sc.register(key.selector(), SelectionKey.OP_READ);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (key.isReadable()) {
            // 客户端连接成功后处理事件
            SocketChannel sc = null;
            try {
                sc = (SocketChannel) key.channel();
                /**
                 * NIO 中 ByteBuffer 很难用，Netty 中 使用 ByteBuf
                 * ByteBuffer 字节数组中只有一个指针，读时，写时，边读边写时都用该指针；写操作后，重置指针，读操作
                 * ByteBuf 中有两个指针，读指针 和 写指针
                 */
                ByteBuffer buffer = ByteBuffer.allocate(512);
                buffer.clear();
                // 读取客户端发送到服务端的数据
                int len = sc.read(buffer);
                if (len != -1) {
                    System.out.println("服务端读取到客户端发送的消息："+new String(buffer.array(), 0, len));
                }
                // 服务端向客户端写消息
                ByteBuffer bufferToWrite = ByteBuffer.wrap("Hello Client".getBytes());
                sc.write(bufferToWrite);
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                if (sc != null) {
                    try {
                        sc.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
