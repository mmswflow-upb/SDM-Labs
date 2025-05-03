package org.example.wschat.service;

import org.example.wschat.dto.Auction;
import org.example.wschat.dto.AuctionMessage;
import org.example.wschat.dto.Bid;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuctionService {
    private final List<Auction> auctions = new ArrayList<>();
    private final SimpMessagingTemplate messagingTemplate;

    public AuctionService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public Auction createAuction(String itemName, double minPrice, int expirationMinutes) {
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(expirationMinutes);
        Auction auction = new Auction(itemName, minPrice, expirationTime);
        auctions.add(auction);
        return auction;
    }

    public List<Auction> getAllActiveAuctions() {
        return auctions.stream()
                .filter(auction -> !auction.isExpired())
                .collect(Collectors.toList());
    }

    public List<Auction> getAllAuctions() {
        return new ArrayList<>(auctions);
    }

    public Optional<Auction> getAuctionById(String id) {
        return auctions.stream()
                .filter(auction -> auction.getId().equals(id))
                .findFirst();
    }

    public boolean placeBid(String auctionId, String bidder, double amount) {
        Optional<Auction> optionalAuction = getAuctionById(auctionId);
        if (!optionalAuction.isPresent()) {
            return false;
        }

        Auction auction = optionalAuction.get();
        if (auction.isExpired() || amount <= auction.getCurrentHighestBid()) {
            return false;
        }

        Bid bid = new Bid(bidder, amount, auctionId);
        boolean success = auction.addBid(bid);

        if (success) {
            // Notify all clients about the updated auction
            AuctionMessage message = new AuctionMessage("AUCTION_UPDATE", auction, "system");
            messagingTemplate.convertAndSend("/topic/auctions", message);
        }

        return success;
    }

    public void checkAuctionExpirations() {
        List<Auction> expiredAuctions = new ArrayList<>();

        for (Auction auction : auctions) {
            if (!auction.isExpired() && LocalDateTime.now().isAfter(auction.getExpirationTime())) {
                auction.checkExpiration();
                expiredAuctions.add(auction);
            }
        }

        for (Auction expiredAuction : expiredAuctions) {
            String winnerMessage = expiredAuction.getWinner() != null ?
                    "Winner: " + expiredAuction.getWinner() : "No bids were placed";
            
            AuctionMessage message = new AuctionMessage(
                    "AUCTION_EXPIRED",
                    "Auction for " + expiredAuction.getItemName() + " has expired. " + winnerMessage,
                    "system"
            );
            messagingTemplate.convertAndSend("/topic/auctions", message);
        }
    }
} 