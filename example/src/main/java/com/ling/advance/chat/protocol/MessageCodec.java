package com.ling.advance.chat.protocol;

import com.ling.advance.chat.message.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * 编解码
 *
 * @author zhangling  2021/5/19 21:46
 */
@Slf4j
public class MessageCodec extends ByteToMessageCodec<Message> {
    @Override
    public void encode(ChannelHandlerContext ctx, Message message, ByteBuf byteBuf) throws Exception {
        // 1. 4 个字节的魔数
        byteBuf.writeBytes(new byte[]{1, 2, 3, 4});
        // 2. 1 个字节的版本号
        byteBuf.writeByte(1);
        // 3. 1 个字节的序列化方式 0：jdk,1:json
        byteBuf.writeByte(0);
        // 4. 1 个字节的指令类型
        byteBuf.writeByte(message.getMessageType());
        // 5. 4 个字节的序号
        byteBuf.writeInt(message.getSequenceId());
        // 除消息正文 消息的固定长度为 15个字节（一般为 2的整数倍）
        // 无意义，对齐填充
        byteBuf.writeByte(0xff);
        // 6. 获取消息内容的字节数组（获取对象的字节数组）
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(message);
        byte[] bytes = bos.toByteArray();
        // 7. 消息长度
        byteBuf.writeInt(bytes.length);
        // 8. 消息正文
        byteBuf.writeBytes(bytes);

    }

    @Override
    public void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {
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

        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
        Message msg = (Message) ois.readObject();
        log.debug("{},{},{},{},{},{}", magicNum, version, serializerType, messageType, sequenceId, length);
        log.debug("{}", msg);

        // 将消息传递给下一个
        list.add(msg);
    }
}
