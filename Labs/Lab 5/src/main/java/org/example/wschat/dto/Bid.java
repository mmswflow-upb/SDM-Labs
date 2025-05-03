package org.example.wschat.dto;

import java.time.LocalDateTime;

public class Bid {
    private String bidder;
    private double amount;
    private String auctionId;
    private LocalDateTime bidTime;

    public Bid(String bidder, double amount, String auctionId) {
        this.bidder = bidder;
        this.amount = amount;
        this.auctionId = auctionId;
        this.bidTime = LocalDateTime.now();
    }

    public String getBidder() {
        return bidder;
    }

    public void setBidder(String bidder) {
        this.bidder = bidder;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(String auctionId) {
        this.auctionId = auctionId;
    }

    public LocalDateTime getBidTime() {
        return bidTime;
    }

    public void setBidTime(LocalDateTime bidTime) {
        this.bidTime = bidTime;
    }
} 