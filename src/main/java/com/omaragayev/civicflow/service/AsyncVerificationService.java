package com.omaragayev.civicflow.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class AsyncVerificationService {

    private static final Logger log = LoggerFactory.getLogger(AsyncVerificationService.class);

    @Async("civicTaskExecutor")
    public CompletableFuture<Void> processPermitVerificationAsync(String trackingNumber, String citizenPin, String permitType) {
        log.info("[ASYNC-START] [{}] Dispatched verification worker for PIN: {}, Type: {}",
                trackingNumber, citizenPin, permitType);

        try {
            // Simulating external governmental verification latency
            Thread.sleep(3000);
            log.info("[ASYNC-COMPLETE] [{}] Automated registry check passed for citizen: {}",
                    trackingNumber, citizenPin);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("[ASYNC-ERROR] [{}] Verification interrupted: {}", trackingNumber, e.getMessage());
        }

        return CompletableFuture.completedFuture(null);
    }
}