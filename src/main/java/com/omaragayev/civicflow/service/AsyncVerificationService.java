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
    public CompletableFuture<Void> performBackgroundVerification(String trackingNumber, String citizenPin) {
        log.info("[ASYNC START] Processing background verification for permit: {} | Citizen PIN: {} | Thread: {}",
                trackingNumber, citizenPin, Thread.currentThread().getName());

        try {
            // Simulate intensive external governmental background registry checks (3 seconds)
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("[ASYNC ERROR] Verification interrupted for tracking: {}", trackingNumber, e);
            return CompletableFuture.completedFuture(null);
        }

        log.info("[ASYNC COMPLETE] Permit {} successfully passed background checks. Status updated to UNDER_REVIEW | Thread: {}",
                trackingNumber, Thread.currentThread().getName());

        return CompletableFuture.completedFuture(null);
    }
}