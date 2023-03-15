package com.ling.advance.chat.message;

import lombok.Data;
import lombok.ToString;

/**
 * 聊天室接收到的消息
 */
@Data
@ToString(callSuper = true)
public class GroupChatResponseMessage extends AbstractResponseMessage {

    private String content;
    private String from;

    public GroupChatResponseMessage(String from, String content) {
        this.content = content;
        this.from = from;
    }

    public GroupChatResponseMessage(boolean success, String reason) {
        super(success, reason);
    }

    @Override
    public int getMessageType() {
        return GroupChatResponseMessage;
    }
}
