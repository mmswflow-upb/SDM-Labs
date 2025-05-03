package org.example.wschat.dto;

public class ChatMessage {

    private String content;
    private String sender;
    //getters and setters

    public ChatMessage(String content, String sender) {
        this.content = content;
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
