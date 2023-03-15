package com.ling.advance.chat.message;

import lombok.Data;
import lombok.ToString;

import java.util.Set;

/**
 * 创建聊天组消息
 */
@Data
@ToString
public class GroupCreateRequestMessage extends Message{

    private String groupName;
    private Set<String> members;

    public GroupCreateRequestMessage(String groupName, Set<String> members) {
        this.groupName = groupName;
        this.members = members;
    }

    @Override
    public int getMessageType() {
        return GroupCreateRequestMessage;
    }
}
