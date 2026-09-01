package com.omaragayev.civicflow.scheduler;

import com.omaragayev.civicflow.entity.PermitApplicationEntity;
import com.omaragayev.civicflow.repository.PermitApplicationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
public class PermitMaintenanceScheduler {

    private static final Logger log = LoggerFactory.getLogger(PermitMaintenanceScheduler.class);
    private static final int BATCH_SIZE = 50;

    private final PermitApplicationRepository permitRepository;

    public PermitMaintenanceScheduler(PermitApplicationRepository permitRepository) {
        this.permitRepository = permitRepository;
    }

    /**
     * Runs every night at 02:00:00 AM (Server Time)
     * Auto-expires pending applications that have been idle for more than 30 days.
     */
    @Scheduled(cron = "0 0 2 * * *")
    @Transactional
    public void cleanupStalePermitApplications() {
        log.info("[BATCH-JOB] Starting nightly stale permit maintenance job on thread: {}", Thread.currentThread().getName());

        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        int pageNumber = 0;
        int totalProcessed = 0;

        Page<PermitApplicationEntity> stalePage;
        do {
            stalePage = permitRepository.findStaleApplications("SUBMITTED", thirtyDaysAgo, PageRequest.of(pageNumber, BATCH_SIZE));

            for (PermitApplicationEntity permit : stalePage.getContent()) {
                permit.setStatus("EXPIRED");
                permit.setDecisionRemarks("System auto-expired: Exceeded 30-day municipal processing SLA threshold.");
                totalProcessed++;
            }

            permitRepository.saveAll(stalePage.getContent());
            pageNumber++;
        } while (stalePage.hasNext());

        log.info("[BATCH-JOB-COMPLETED] Nightly maintenance finalized. Total expired permits processed: {}", totalProcessed);
    }

    /**
     * Heartbeat health check running every 30 minutes.
     */
    @Scheduled(fixedDelay = 1800000, initialDelay = 60000)
    public void logPermitProcessingHeartbeat() {
        long pendingCount = permitRepository.count();
        log.info("[HEARTBEAT] CivicFlow operational metrics: Total persisted permits in registry: {}", pendingCount);
    }
}