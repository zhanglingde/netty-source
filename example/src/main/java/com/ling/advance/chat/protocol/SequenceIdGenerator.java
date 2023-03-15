package com.ling.advance.chat.protocol;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhangling 2021/5/24 16:01
 */
public class SequenceIdGenerator {
    private static final AtomicInteger id = new AtomicInteger();

    public static int nextId() {
        return id.incrementAndGet();
    }
}
