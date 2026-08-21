package com.omaragayev.civicflow.controller;

import com.omaragayev.civicflow.model.PermitType;
import com.omaragayev.civicflow.service.PermitCatalogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/permits/catalog")
@Tag(name = "Permit Catalog", description = "Public service catalog and fee schedule endpoints backed by Redis distributed cache")
public class PermitCatalogController {

    private final PermitCatalogService catalogService;

    public PermitCatalogController(PermitCatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping
    @Operation(summary = "Retrieve active permit categories", description = "Uses Cache-Aside pattern via Redis. Subsequent requests return in milliseconds.")
    public ResponseEntity<List<PermitType>> getCatalog() {
        return ResponseEntity.ok(catalogService.getAllPermitTypes());
    }

    @PostMapping("/evict")
    @Operation(summary = "Evict permit catalog cache", description = "Clears stale cache entries from Redis.")
    public ResponseEntity<String> evictCache() {
        catalogService.evictPermitCatalogCache();
        return ResponseEntity.ok("Permit catalog cache successfully evicted from Redis.");
    }
}