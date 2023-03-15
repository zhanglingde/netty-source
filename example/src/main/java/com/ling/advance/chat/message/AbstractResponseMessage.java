package com.ling.advance.chat.message;

import lombok.Data;
import lombok.ToString;

/**
 * @author zhangling 2021/5/20 15:23
 */
@Data
@ToString(callSuper = true)
public abstract class AbstractResponseMessage extends Message{
    private boolean success;
    private String reason;

    public AbstractResponseMessage() {
    }

    public AbstractResponseMessage(boolean success, String reason) {
        this.success = success;
        this.reason = reason;
    }
}
