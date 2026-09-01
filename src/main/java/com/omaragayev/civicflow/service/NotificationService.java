package com.omaragayev.civicflow.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    @Async("civicTaskExecutor")
    public void sendPermitSubmissionNotification(String citizenEmail, String trackingNumber) {
        log.info("[ASYNC-TASK] Executing permit submission notification on thread: {}", Thread.currentThread().getName());
        try {
            // Simulate SMTP network latency
            Thread.sleep(1500);
            log.info("[NOTIFICATION-SENT] Confirmation email dispatched to [{}] for tracking [{}]", citizenEmail, trackingNumber);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("[NOTIFICATION-ERROR] Notification interrupted for tracking: {}", trackingNumber, e);
        }
    }

    @Async("civicTaskExecutor")
    public CompletableFuture<String> generateDigitalPermitDocument(String trackingNumber, String status) {
        log.info("[ASYNC-TASK] Generating digital permit document on thread: {}", Thread.currentThread().getName());
        try {
            // Simulate document rendering & digital signature
            Thread.sleep(1000);
            String documentUri = "https://documents.civicflow.gov/permits/" + trackingNumber + ".pdf";
            log.info("[DOC-GENERATED] Digital permit ready at [{}]", documentUri);
            return CompletableFuture.completedFuture(documentUri);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("[DOC-ERROR] Document generation failed for tracking: {}", trackingNumber, e);
            return CompletableFuture.failedFuture(e);
        }
    }
}