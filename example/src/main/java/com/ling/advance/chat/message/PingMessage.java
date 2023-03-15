package com.ling.advance.chat.message;

/**
 * @author zhangling  2021/5/22 10:19
 */
public class PingMessage extends Message{


    @Override
    public int getMessageType() {
        return PingMessage;
    }
}
