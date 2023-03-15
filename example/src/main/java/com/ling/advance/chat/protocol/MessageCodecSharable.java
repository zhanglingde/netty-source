package com.ling.advance.chat.protocol;

import com.ling.advance.chat.config.Config;
import com.ling.advance.chat.message.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@ChannelHandler.Sharable
/**
 * 必须和 LengthFieldBasedFrameDecoder 一起使用，确保接收到的 ByteBUf 是完整的
 */
public class MessageCodecSharable extends MessageToMessageCodec<ByteBuf, Message> {

    @Override
    public void encode(ChannelHandlerContext ctx, Message msg, List<Object> out) throws Exception {
        ByteBuf buffer = ctx.alloc().buffer();
        // 1. 4 个字节的魔数
        buffer.writeBytes(new byte[]{1, 2, 3, 4});
        // 2. 1 个字节的版本号
        buffer.writeByte(1);
        // 3. 1 个字节的序列化方式 0：jdk,1:json
        buffer.writeByte(Config.getSerializerAlgorithm().ordinal());
        // 4. 1 个字节的指令类型
        buffer.writeByte(msg.getMessageType());
        // 5. 4 个字节的序号
        buffer.writeInt(msg.getSequenceId());
        // 除消息正文 消息的固定长度为 15个字节（一般为 2的整数倍）
        // 无意义，对齐填充
        buffer.writeByte(0xff);
        // 6. 获取消息内容的字节数组（获取对象的字节数组）
        byte[] bytes = Config.getSerializerAlgorithm().serializer(msg);
        // 7. 消息长度
        buffer.writeInt(bytes.length);
        // 8. 消息正文
        buffer.writeBytes(bytes);
        // 将 buffer 传递给下一个处理器
        out.add(buffer);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) throws Exception {
        // 解码消息 与编码消息顺序一致
        int magicNum = byteBuf.readInt();
        byte version = byteBuf.readByte();
        byte serializerType = byteBuf.readByte();
        byte messageType = byteBuf.readByte();
        int sequenceId = byteBuf.readInt();
        byteBuf.readByte();
        int length = byteBuf.readInt();
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes, 0, length);

        // 找到反序列化算法
        Serializer.Algorithm algorithm = Serializer.Algorithm.values()[serializerType];
        Message msg = algorithm.deserializer(Message.getMessageClass(messageType), bytes);
        log.debug("{},{},{},{},{},{}", magicNum, version, serializerType, messageType, sequenceId, length);
        log.debug("解码器解码消息：{}", msg);

        // 将消息传递给下一个
        out.add(msg);
    }
}
