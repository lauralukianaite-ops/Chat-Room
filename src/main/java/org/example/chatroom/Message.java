package org.example.chatroom;

import java.io.Serializable;

public class Message implements Serializable {
    private String sender;
    private String content;
    private String type;

    public Message(String sender, String content, String type) {
        this.sender = sender;
        this.content = content;
        this.type = type;
    }

    public String getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }

    public String getType() {
        return type;
    }
}
