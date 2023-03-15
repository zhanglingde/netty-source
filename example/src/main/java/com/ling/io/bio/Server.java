package com.ling.io.bio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * bio服务器
 */
public class Server {
    public static void main(String[] args) throws IOException {
        // 创建一个 BIO 服务器
        ServerSocket ss = new ServerSocket();
        ss.bind(new InetSocketAddress("127.0.0.1", 8888));
        while (true) {
            // 阻塞方法，接收客户端连接
            Socket s = ss.accept();

            // 每当有一个客户端连接至服务器时，启动一个新的线程
            new Thread(() -> {
                handle(s);
            }).start();
        }
    }

    private static void handle(Socket s) {
        try {
            byte[] bytes = new byte[1024];
            // 处理客户端发送过来的消息
            int len = s.getInputStream().read(bytes);
            System.out.println(new String(bytes, 0, len));

            // 服务器往客户端发送
            s.getOutputStream().write(bytes, 0, len);
            s.getOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
