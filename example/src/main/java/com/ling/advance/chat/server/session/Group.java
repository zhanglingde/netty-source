package com.ling.advance.chat.server.session;

import lombok.Data;
import lombok.ToString;

import java.util.Collections;
import java.util.Set;

/**
 * 聊天室
 */
@Data
@ToString
public class Group {
    // 聊天室名称
    private String name;
    // 聊天室成员
    private Set<String> members;

    public static final Group EMPTY_GROUP = new Group("empty", Collections.emptySet());

    public Group(String name,Set<String> members) {
        this.name = name;
        this.members = members;
    }

}
