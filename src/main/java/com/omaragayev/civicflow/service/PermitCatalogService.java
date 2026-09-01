package com.omaragayev.civicflow.service;

import com.omaragayev.civicflow.dto.response.PermitCatalogItemResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermitCatalogService {

    private static final Logger log = LoggerFactory.getLogger(PermitCatalogService.class);

    @Cacheable(value = "permitCatalog", key = "'all'")
    public List<PermitCatalogItemResponse> getAllPermitTypes() {
        log.warn("[CACHE-MISS] Fetching municipal permit catalog from source (Populating Redis cache)...");
        return List.of(
                new PermitCatalogItemResponse("BUILDING_RENOVATION", "Residential Renovation Permit", 150.0, 5),
                new PermitCatalogItemResponse("COMMERCIAL_SIGNAGE", "Commercial Signage & Banner Permit", 75.0, 2),
                new PermitCatalogItemResponse("SOLAR_PANEL_INSTALLATION", "Green Energy Solar Permit", 0.0, 1),
                new PermitCatalogItemResponse("SPECIAL_EVENT_OCCUPANCY", "Public Gathering & Event Permit", 250.0, 10)
        );
    }

    @CacheEvict(value = "permitCatalog", allEntries = true)
    public void evictPermitCatalogCache() {
        log.info("[CACHE-EVICT] Evicted 'permitCatalog' cache entries from Redis cluster.");
    }
}