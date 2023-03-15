package com.ling.advance.chat.message;

import lombok.Data;
import lombok.ToString;

/**
 * @author zhangling 2021/5/21 9:17
 */
@Data
@ToString
public class GroupChatRequestMessage extends Message{

    private String groupName;
    private String content;
    private String from;

    public GroupChatRequestMessage(String groupName, String content, String from) {
        this.groupName = groupName;
        this.content = content;
        this.from = from;
    }

    @Override
    public int getMessageType() {
        return GroupChatRequestMessage;
    }
}
