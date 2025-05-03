package org.example.wschat.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class Auction {
    private String id;
    private String itemName;
    private double minPrice;
    private LocalDateTime expirationTime;
    private List<Bid> bids;
    private boolean expired;
    private String winner;

    public Auction(String itemName, double minPrice, LocalDateTime expirationTime) {
        this.id = UUID.randomUUID().toString();
        this.itemName = itemName;
        this.minPrice = minPrice;
        this.expirationTime = expirationTime;
        this.bids = new ArrayList<>();
        this.expired = false;
        this.winner = null;
    }

    public String getId() {
        return id;
    }

    public String getItemName() {
        return itemName;
    }

    public double getMinPrice() {
        return minPrice;
    }

    public LocalDateTime getExpirationTime() {
        return expirationTime;
    }

    public List<Bid> getBids() {
        return bids;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public double getCurrentHighestBid() {
        if (bids.isEmpty()) {
            return minPrice;
        }
        return bids.stream()
                .max(Comparator.comparingDouble(Bid::getAmount))
                .get()
                .getAmount();
    }

    public boolean addBid(Bid bid) {
        if (expired || bid.getAmount() <= getCurrentHighestBid()) {
            return false;
        }
        bids.add(bid);
        return true;
    }

    public void checkExpiration() {
        if (!expired && LocalDateTime.now().isAfter(expirationTime)) {
            expired = true;
            if (!bids.isEmpty()) {
                Bid highestBid = bids.stream()
                        .max(Comparator.comparingDouble(Bid::getAmount))
                        .get();
                winner = highestBid.getBidder();
            }
        }
    }
} 