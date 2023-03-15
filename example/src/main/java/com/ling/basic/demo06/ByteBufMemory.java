package com.ling.basic.demo06;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

/**
 * 直接内存 与堆内存
 * @author zhangling 2021/5/18 10:13
 */
public class ByteBufMemory {
    public static void main(String[] args) {
        // 直接内存
        ByteBuf directBuffer = ByteBufAllocator.DEFAULT.buffer();
        // 堆内存
        ByteBuf heapBuffer = ByteBufAllocator.DEFAULT.heapBuffer();

        System.out.println(directBuffer.getClass());
        System.out.println(heapBuffer.getClass());
    }
}
