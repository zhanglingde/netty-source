package com.ling.advance.chat.message;

import lombok.Data;
import lombok.ToString;

/**
 * @author zhangling 2021/5/21 9:59
 */
@Data
@ToString(callSuper = true)
public class GroupJoinResponseMessage extends AbstractResponseMessage{

    public GroupJoinResponseMessage(boolean success,String reason) {
        super(success, reason);
    }

    @Override
    public int getMessageType() {
        return GroupJoinResponseMessage;
    }
}
