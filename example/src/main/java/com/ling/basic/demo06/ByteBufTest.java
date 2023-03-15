package com.ling.basic.demo06;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import lombok.extern.slf4j.Slf4j;

/**
 * 创建 ByteBuf
 * @author zhangling 2021/5/18 9:53
 */
@Slf4j
public class ByteBufTest {
    public static void main(String[] args) {
        // 不指定容量时默认为 256
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(10);
        ByteBufUtils.log(buffer);

        buffer.writeByte(97);
        buffer.writeByte(98);
        ByteBufUtils.log(buffer);
    }
}
