package com.ling.advance.chat.message;

import lombok.Data;
import lombok.ToString;

import java.util.Set;

/**
 * 获取聊天室成员返回消息
 */
@Data
@ToString(callSuper = true)
public class GroupMembersResponseMessage extends AbstractResponseMessage{

    private Set<String> members;

    public GroupMembersResponseMessage(boolean success, String reason) {
        super(success, reason);
    }

    public GroupMembersResponseMessage(Set<String> members) {
        this.members = members;
    }

    @Override
    public int getMessageType() {
        return GroupMembersResponseMessage;
    }
}
