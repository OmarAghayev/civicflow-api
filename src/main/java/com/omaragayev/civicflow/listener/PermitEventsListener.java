package com.omaragayev.civicflow.listener;

import com.omaragayev.civicflow.event.PermitSubmittedEvent;
import com.omaragayev.civicflow.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class PermitEventsListener {

    private static final Logger log = LoggerFactory.getLogger(PermitEventsListener.class);
    private final NotificationService notificationService;

    public PermitEventsListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Async("civicTaskExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePermitSubmittedNotification(PermitSubmittedEvent event) {
        log.info("[EVENT-RECEIVED] Processing PermitSubmittedEvent for tracking: {} on thread: {}",
                event.trackingNumber(), Thread.currentThread().getName());

        notificationService.sendPermitSubmissionNotification(event.citizenEmail(), event.trackingNumber());
        notificationService.generateDigitalPermitDocument(event.trackingNumber(), "SUBMITTED");
    }

    @Async("civicTaskExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePermitSubmittedAuditLog(PermitSubmittedEvent event) {
        log.info("[AUDIT-LOG] Immutable audit entry recorded: Permit [{}] created for Citizen PIN [{}] at [{}]",
                event.trackingNumber(), event.citizenPin(), event.timestamp());
    }
}