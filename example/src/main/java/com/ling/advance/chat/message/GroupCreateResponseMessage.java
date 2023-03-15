package com.ling.advance.chat.message;

/**
 * 创建聊天室返回消息
 */
public class GroupCreateResponseMessage extends AbstractResponseMessage{

    public GroupCreateResponseMessage(boolean success,String reason) {
        super(success,reason);
    }

    @Override
    public int getMessageType() {
        return GroupCreateResponseMessage;
    }
}
