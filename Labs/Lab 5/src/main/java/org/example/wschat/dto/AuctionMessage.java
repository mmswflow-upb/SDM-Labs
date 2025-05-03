package org.example.wschat.dto;

public class AuctionMessage {
    private String type; // "NEW_AUCTION", "BID", "AUCTION_EXPIRED", "AUCTION_UPDATE", "LOGIN"
    private Object content;
    private String sender;

    public AuctionMessage() {
    }

    public AuctionMessage(String type, Object content, String sender) {
        this.type = type;
        this.content = content;
        this.sender = sender;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
} 