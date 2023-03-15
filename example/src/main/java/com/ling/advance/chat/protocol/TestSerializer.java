package com.ling.advance.chat.protocol;


import com.ling.advance.chat.config.Config;
import com.ling.advance.chat.message.LoginRequestMessage;
import com.ling.advance.chat.message.Message;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.logging.LoggingHandler;

/**
 * 测试序列化
 */
public class TestSerializer {
    public static void main(String[] args) {
        MessageCodecSharable CODEC = new MessageCodecSharable();
        LoggingHandler LOGGING_HANDLER = new LoggingHandler();
        EmbeddedChannel channel = new EmbeddedChannel(LOGGING_HANDLER,CODEC,LOGGING_HANDLER);

        // 测试编码（序列化）
        LoginRequestMessage message = new LoginRequestMessage("zhangsan", "123");
        // channel.writeOutbound(message);

        // 测试入站（将 buffer 中的数据反序列化成对象）
        ByteBuf buffer = messageToBytes(message);
        channel.writeInbound(buffer);
    }

    public static ByteBuf messageToBytes(Message message) {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        // 1. 4 个字节的魔数
        buffer.writeBytes(new byte[]{1, 2, 3, 4});
        // 2. 1 个字节的版本号
        buffer.writeByte(1);
        // 3. 1 个字节的序列化方式 0：jdk,1:json
        buffer.writeByte(Config.getSerializerAlgorithm().ordinal());
        // 4. 1 个字节的指令类型
        buffer.writeByte(message.getMessageType());
        // 5. 4 个字节的序号
        buffer.writeInt(message.getSequenceId());
        // 除消息正文 消息的固定长度为 15个字节（一般为 2的整数倍）
        // 无意义，对齐填充
        buffer.writeByte(0xff);
        // 6. 获取消息内容的字节数组（获取对象的字节数组）
        byte[] bytes = Config.getSerializerAlgorithm().serializer(message);
        // 7. 消息长度
        buffer.writeInt(bytes.length);
        // 8. 消息正文
        buffer.writeBytes(bytes);

        return buffer;
    }
}
