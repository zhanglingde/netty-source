package com.ling.basic.demo06;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import static com.ling.basic.demo06.ByteBufUtils.log;

/**
 * splice 切片
 * @author zhangling 2021/5/18 14:16
 */
public class ByteBufSliceTest {
    public static void main(String[] args) {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(10);
        buffer.writeBytes(new byte[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j'});
        log(buffer);

        // 在切片过程中，没有发生数据复制，而是逻辑分区，使用的还是原来的物理内存
        ByteBuf f1 = buffer.slice(0, 5);
        f1.retain();            // 需要使用的时候 计数 +1 （等到不需要使用的时候自己释放 -1）
        ByteBuf f2 = buffer.slice(5, 5);
        f2.retain();
        log(f1);
        log(f2);

        System.out.println("释放原有内存");
        buffer.release();
        log(f1);

        // 修改的是同一块内存
        System.out.println("=============================");
        f1.setByte(0, 'b');
        log(buffer);
        log(f1);

        f1.release();
        f2.release();
    }
}
