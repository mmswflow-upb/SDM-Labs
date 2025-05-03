package org.example.wschat.controllers;

import org.example.wschat.service.AuctionService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AuctionScheduler {
    
    private final AuctionService auctionService;
    
    public AuctionScheduler(AuctionService auctionService) {
        this.auctionService = auctionService;
    }
    
    @Scheduled(fixedDelay = 5000) // Check every 5 seconds
    public void checkExpirations() {
        auctionService.checkAuctionExpirations();
    }
} 