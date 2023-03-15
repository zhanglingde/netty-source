package com.ling.basic.demo06;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

/**
 * @author zhangling 2021/5/18 15:08
 */
public class ByteBufUnpooled {
    public static void main(String[] args) {
        ByteBuf buf1 = ByteBufAllocator.DEFAULT.buffer(5);
        buf1.writeBytes(new byte[]{1,2,3,4,5});
        ByteBuf buf2 = ByteBufAllocator.DEFAULT.buffer(5);
        buf1.writeBytes(new byte[]{6,7,8,9,10});

        // 当包装 ByteBuf 个数超过一个时，底层使用了 CompositeByteBuf
        ByteBuf buf3 = Unpooled.wrappedBuffer(buf1, buf2);
        System.out.println(ByteBufUtil.prettyHexDump(buf3));
    }
}
