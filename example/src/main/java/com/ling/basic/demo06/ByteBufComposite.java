package com.ling.basic.demo06;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;

import static com.ling.basic.demo06.ByteBufUtils.log;

/**
 * @author zhangling 2021/5/18 14:35
 */
public class ByteBufComposite {
    public static void main(String[] args) {
        ByteBuf buf1 = ByteBufAllocator.DEFAULT.buffer();
        buf1.writeBytes(new byte[]{1, 2, 3, 4, 5});

        ByteBuf buf2 = ByteBufAllocator.DEFAULT.buffer();
        buf1.writeBytes(new byte[]{6, 7, 8, 9, 10});

        // 发生了数据复制，如果数据量过会对性能产生影响
        //ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        //buffer.writeBytes(buf1).writeBytes(buf2);
        //log(buffer);

        CompositeByteBuf buffer = ByteBufAllocator.DEFAULT.compositeBuffer();
        buffer.addComponents(true, buf1, buf2);
        log(buffer);

    }

}
