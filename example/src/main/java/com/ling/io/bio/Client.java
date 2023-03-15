package com.ling.io.bio;

import java.io.IOException;
import java.net.Socket;

/**
 * bio半双工，读时不能写，写时不能读
 */
public class Client {
    public static void main(String[] args) throws IOException {
        // 客户端连接到服务器端，s可以向服务器读或写内容
        Socket s = new Socket("127.0.0.1",8888);
        // 通过该socket往服务器写内容
        s.getOutputStream().write("HelloServer".getBytes());
        s.getOutputStream().flush();
        // s.getOutputStream().close();
        System.out.println("write over,waiting for msg back...");
        byte[] bytes = new byte[1024];
        // 每次从服务器读取字节数组大小的内容，内容超过1个字节数组时，在本地做处理，先存好，读取完后在合并内容
        int len = s.getInputStream().read(bytes);
        System.out.println("客户端接收到服务端的消息："+new String(bytes,0,len));
        s.close();

    }
}
