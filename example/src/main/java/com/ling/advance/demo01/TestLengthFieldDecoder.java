package com.ling.advance.demo01;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * 预设长度解决
 *
 * @author zhangling 2021/5/19 15:19
 */
public class TestLengthFieldDecoder {
    public static void main(String[] args) {
        EmbeddedChannel channel = new EmbeddedChannel(
                new LengthFieldBasedFrameDecoder(
                        1024, 0, 4, 1, 4),
                new LoggingHandler(LogLevel.DEBUG)
        );
        // 4 个字节的内容长度（消息实际内容的长度）
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        send(buffer, "Hello, world");
        send(buffer, "Hi!");

        // 将 buffer 的消息写入 channel
        channel.writeInbound(buffer);


    }

    /**
     * 发送消息
     *
     * @param buffer
     * @param msg
     */
    private static void send(ByteBuf buffer, String msg) {
        byte[] bytes = msg.getBytes();
        int length = msg.length();
        buffer.writeInt(length);                    // 先写入消息长度
        buffer.writeByte(1);                        // 版本号
        buffer.writeBytes(bytes);                   // 写入消息内容
    }
}
