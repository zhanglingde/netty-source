package com.ling.advance.chat.message;

import lombok.Data;
import lombok.ToString;

/**
 * @author zhangling  2021/5/20 21:21
 */
@Data
@ToString(callSuper = true)
public class ChatResponseMessage extends AbstractResponseMessage{
    private String from;
    private String content;

    public ChatResponseMessage(String from, String content) {
        this.from = from;
        this.content = content;
    }

    public ChatResponseMessage(boolean success, String reason) {
        super(success, reason);
    }

    @Override
    public int getMessageType() {
        return ChatResponseMessage;
    }
}
