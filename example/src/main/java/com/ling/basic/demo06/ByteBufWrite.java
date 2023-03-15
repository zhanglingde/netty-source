package com.ling.basic.demo06;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

/**
 * 写入
 * @author zhangling 2021/5/18 10:42
 */
public class ByteBufWrite {
    public static void main(String[] args) {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(10);
        // 写入 4 个字节
        buffer.writeBytes(new byte[]{1, 2, 3, 4});
        ByteBufUtils.log(buffer);

        // 在写入一个 int 整数，int 4 个字节
        buffer.writeInt(5);
        ByteBufUtils.log(buffer);

        // 在写入一个 int 整数，超过 10，触发扩容
        buffer.writeInt(6);
        ByteBufUtils.log(buffer);

        System.out.println("==================");

        // 读取 4 个字节
        System.out.println(buffer.readByte());
        System.out.println(buffer.readByte());
        System.out.println(buffer.readByte());
        System.out.println(buffer.readByte());

        // 标记读指针
        buffer.markReaderIndex();
        System.out.println("buffer.readInt() = " + buffer.readInt());
        ByteBufUtils.log(buffer);
        // 重置读指针到标记位置
        buffer.resetReaderIndex();
        ByteBufUtils.log(buffer);
        System.out.println("buffer.readInt() = " + buffer.readInt());

    }
}
