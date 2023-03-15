package com.ling.advance.chat.message;

import lombok.Data;
import lombok.ToString;

/**
 * 加入聊天室消息
 */
@Data
@ToString
public class GroupJoinRequestMessage extends Message{

    private String groupName;
    private String username;

    public GroupJoinRequestMessage(String groupName, String username) {
        this.groupName = groupName;
        this.username = username;
    }

    @Override
    public int getMessageType() {
        return GroupJoinRequestMessage;
    }
}
