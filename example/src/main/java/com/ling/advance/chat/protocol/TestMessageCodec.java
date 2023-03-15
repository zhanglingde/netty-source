package com.ling.advance.chat.protocol;

import com.ling.advance.chat.message.LoginRequestMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author zhangling  2021/5/19 22:23
 */
public class TestMessageCodec {
    public static void main(String[] args) throws Exception {
        EmbeddedChannel channel = new EmbeddedChannel(
                new LoggingHandler(),
                // 解决粘包半包 固定字段16个字节，偏移12个，长度4个
                new LengthFieldBasedFrameDecoder(1024,12,4,0,0),
                new MessageCodec()
        );

        // encode（出站处理器，先经过MessageCodec对对象编码，然后打印日志）
        LoginRequestMessage message = new LoginRequestMessage("zhangsan", "123");
        channel.writeOutbound(message);

        System.out.println("=================");
        // decode（入站）
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        new MessageCodec().encode(null,message,buffer); // 调用 encode 方法将数据编码后写入buf中
        // channel.writeInbound(buffer);                       // 入站

        ByteBuf s1 = buffer.slice(0, 100);
        ByteBuf s2 = buffer.slice(100, buffer.readableBytes()-100);
        // 添加LengthFieldBasedFrameDecoder后，如果没有接收到指定长度的消息内容，会等待接收
        s1.retain();                // 引用计数 2
        // 半包现象，不加LengthFieldBasedFrameDecoder，只发送s1,数据发送不完整，不能正确解析
        channel.writeInbound(s1);       // release 1
        channel.writeInbound(s2);


    }
}
