package com.ling.advance.chat.message;

import lombok.Data;
import lombok.ToString;

/**
 * @author zhangling 2021/5/20 17:30
 */
@Data
@ToString
public class ChatRequestMessage extends Message{

    private String content;
    private String from;
    private String to;

    public ChatRequestMessage() {
    }

    public ChatRequestMessage(String from, String to,String content) {
        this.content = content;
        this.from = from;
        this.to = to;
    }

    @Override
    public int getMessageType() {
        return ChatRequestMessage;
    }
}
