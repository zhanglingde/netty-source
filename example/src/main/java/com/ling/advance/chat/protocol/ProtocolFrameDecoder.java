package com.ling.advance.chat.protocol;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * 对 LengthFieldBasedFrameDecoder 包装一下，简化构造方法，原先构造方法有太多参数
 * @author zhangling 2021/5/20 14:44
 */
public class ProtocolFrameDecoder extends LengthFieldBasedFrameDecoder {

    public ProtocolFrameDecoder() {
        this(1024,12,4,0,0);
    }

    public ProtocolFrameDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }
}
