package com.ling.advance.chat.message;

import lombok.Data;
import lombok.ToString;

/**
 * 退出聊天室 结果消息
 */
@Data
@ToString(callSuper = true)
public class GroupQuitResponseMessage extends AbstractResponseMessage{

    public GroupQuitResponseMessage(boolean success, String reason) {
        super(success, reason);
    }

    @Override
    public int getMessageType() {
        return GroupQuitResponseMessage;
    }
}
