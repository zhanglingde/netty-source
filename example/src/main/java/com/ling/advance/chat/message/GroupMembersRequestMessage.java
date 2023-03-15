package com.ling.advance.chat.message;

import lombok.Data;
import lombok.ToString;

/**
 * 获得聊天室成员消息
 */
@Data
@ToString
public class GroupMembersRequestMessage extends Message{

    private String groupName;

    public GroupMembersRequestMessage(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public int getMessageType() {
        return GroupMembersRequestMessage;
    }
}
