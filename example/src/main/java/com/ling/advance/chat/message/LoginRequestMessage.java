package com.ling.advance.chat.message;

import lombok.Data;
import lombok.ToString;

/**
 * @author zhangling  2021/5/19 22:25
 */
@Data
@ToString
public class LoginRequestMessage extends Message{

    private String username;
    private String password;

    public LoginRequestMessage() {
    }

    public LoginRequestMessage(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public int getMessageType() {
        return LoginRequestMessage;
    }
}
