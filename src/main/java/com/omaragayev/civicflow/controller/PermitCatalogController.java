package com.omaragayev.civicflow.controller;

import com.omaragayev.civicflow.dto.response.PermitCatalogItemResponse;
import com.omaragayev.civicflow.service.PermitCatalogService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/permits/catalog")
public class PermitCatalogController {

    private final PermitCatalogService catalogService;

    public PermitCatalogController(PermitCatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping
    public ResponseEntity<List<PermitCatalogItemResponse>> getAllPermitTypes() {
        List<PermitCatalogItemResponse> catalog = catalogService.getAllPermitTypes();
        return ResponseEntity.ok(catalog);
    }

    @PostMapping("/evict-cache")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> evictCatalogCache() {
        catalogService.evictPermitCatalogCache();
        return ResponseEntity.ok("Municipal permit catalog cache successfully evicted from Redis.");
    }
}