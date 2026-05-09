package org.example.chatroom;

public class Message {
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
}
