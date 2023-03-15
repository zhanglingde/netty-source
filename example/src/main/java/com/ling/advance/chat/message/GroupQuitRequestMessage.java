package com.ling.advance.chat.message;

import lombok.Data;
import lombok.ToString;


/**
 * 退出聊天室
 */
@Data
@ToString
public class GroupQuitRequestMessage extends Message{

    private String groupName;
    private String username;

    public GroupQuitRequestMessage(String groupName, String username) {
        this.groupName = groupName;
        this.username = username;
    }

    @Override
    public int getMessageType() {
        return GroupQuitRequestMessage;
    }
}
