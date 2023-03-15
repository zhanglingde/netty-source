package com.ling.advance.chat.message;

import lombok.Data;
import lombok.ToString;

/**
 * Rpc 响应消息
 */
@Data
@ToString
public class RpcResponseMessage extends Message{

    /**
     * 返回值
     */
    private Object returnValue;

    /**
     * 异常值
     */
    private Exception exceptionValue;

    @Override
    public int getMessageType() {
        return RPC_MESSAGE_TYPE_RESPONSE;
    }
}
