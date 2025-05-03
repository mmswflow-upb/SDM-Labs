package org.example.wschat.controllers;

import org.example.wschat.dto.Auction;
import org.example.wschat.dto.AuctionMessage;
import org.example.wschat.dto.User;
import org.example.wschat.service.AuctionService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

@Controller
public class AuctionController {
    private final AuctionService auctionService;
    private final SimpMessagingTemplate messagingTemplate;
    private final Map<String, User> connectedUsers = new HashMap<>();

    public AuctionController(AuctionService auctionService, SimpMessagingTemplate messagingTemplate) {
        this.auctionService = auctionService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/auction.login")
    public void login(AuctionMessage message) {
        String username = message.getSender();
        User user = new User(username);
        connectedUsers.put(username, user);

        // Send a welcome message back to the user
        AuctionMessage welcomeMessage = new AuctionMessage(
                "LOGIN_SUCCESS",
                "Welcome " + username + "! You are logged in as " + user.getRole(),
                "system"
        );
        messagingTemplate.convertAndSendToUser(username, "/queue/private", welcomeMessage);

        // Send the list of active auctions to the user
        AuctionMessage auctionsMessage = new AuctionMessage(
                "AUCTIONS_LIST",
                auctionService.getAllActiveAuctions(),
                "system"
        );
        messagingTemplate.convertAndSend("/topic/auctions", auctionsMessage);
    }

    @MessageMapping("/auction.create")
    public void createAuction(AuctionMessage message) {
        String username = message.getSender();
        User user = connectedUsers.get(username);

        if (user != null && user.isAdmin()) {
            Map<String, Object> content = (Map<String, Object>) message.getContent();
            String itemName = (String) content.get("itemName");
            double minPrice = Double.parseDouble(content.get("minPrice").toString());
            int expirationMinutes = Integer.parseInt(content.get("expirationMinutes").toString());

            Auction auction = auctionService.createAuction(itemName, minPrice, expirationMinutes);

            // Send notification about new auction to all users
            AuctionMessage newAuctionMessage = new AuctionMessage(
                    "NEW_AUCTION",
                    auction,
                    "system"
            );
            messagingTemplate.convertAndSend("/topic/auctions", newAuctionMessage);
        } else {
            // Send error message to user
            AuctionMessage errorMessage = new AuctionMessage(
                    "ERROR",
                    "Only admin can create auctions",
                    "system"
            );
            messagingTemplate.convertAndSendToUser(username, "/queue/private", errorMessage);
        }
    }

    @MessageMapping("/auction.bid")
    public void placeBid(AuctionMessage message) {
        String username = message.getSender();
        Map<String, Object> content = (Map<String, Object>) message.getContent();
        String auctionId = (String) content.get("auctionId");
        double amount = Double.parseDouble(content.get("amount").toString());

        boolean success = auctionService.placeBid(auctionId, username, amount);

        if (!success) {
            // Send error message to user
            AuctionMessage errorMessage = new AuctionMessage(
                    "BID_ERROR",
                    "Your bid was not accepted. It must be higher than the current highest bid.",
                    "system"
            );
            messagingTemplate.convertAndSendToUser(username, "/queue/private", errorMessage);
        }
    }

    @MessageMapping("/auction.getActiveAuctions")
    public void getActiveAuctions() {
        // Send the list of active auctions to all users
        AuctionMessage auctionsMessage = new AuctionMessage(
                "AUCTIONS_LIST",
                auctionService.getAllActiveAuctions(),
                "system"
        );
        messagingTemplate.convertAndSend("/topic/auctions", auctionsMessage);
    }
} 