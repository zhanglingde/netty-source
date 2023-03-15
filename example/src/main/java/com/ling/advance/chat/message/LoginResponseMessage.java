package com.ling.advance.chat.message;

import lombok.Data;
import lombok.ToString;

/**
 * @author zhangling 2021/5/20 15:21
 */
@Data
@ToString(callSuper = true)             // 调用父类toString
public class LoginResponseMessage extends AbstractResponseMessage{

    public LoginResponseMessage(boolean success, String reason) {
        super(success, reason);
    }

    @Override
    public int getMessageType() {
        return LoginResponseMessage;
    }
}
