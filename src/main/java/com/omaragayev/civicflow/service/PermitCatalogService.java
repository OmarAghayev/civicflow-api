package com.omaragayev.civicflow.service;

import com.omaragayev.civicflow.model.PermitType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PermitCatalogService {

    private static final Logger log = LoggerFactory.getLogger(PermitCatalogService.class);

    @Cacheable(value = "permitCatalogs", key = "'all_permits'")
    public List<PermitType> getAllPermitTypes() {
        log.info("[CACHE MISS] Fetching permit catalog from database layer... (Simulating 2s I/O latency)");

        try {
            // Simulate slow relational database I/O latency (2 seconds)
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return List.of(
                new PermitType("COMMERCIAL_LICENSE", "Commercial Business Operation Permit", BigDecimal.valueOf(250.00), 5),
                new PermitType("CONSTRUCTION_PERMIT", "Urban Construction and Renovation License", BigDecimal.valueOf(1200.00), 15),
                new PermitType("EVENT_HOSTING", "Public Space Event and Gathering Permit", BigDecimal.valueOf(75.00), 2)
        );
    }

    @CacheEvict(value = "permitCatalogs", allEntries = true)
    public void evictPermitCatalogCache() {
        log.info("[CACHE EVICT] Purging permit catalogs from Redis cache due to administrative catalog update.");
    }
}