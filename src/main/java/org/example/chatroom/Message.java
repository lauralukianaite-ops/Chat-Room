package org.example.chatroom;

import java.io.Serializable;

public class Message implements Serializable {
    private String sender;
    private String content;
    private String type;
    private String room;

    public Message(String sender, String content, String type, String room) {
        this.sender = sender;
        this.content = content;
        this.type = type;
        this.room = room;
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

    public String getRoom() {
        return room;
    }
}
