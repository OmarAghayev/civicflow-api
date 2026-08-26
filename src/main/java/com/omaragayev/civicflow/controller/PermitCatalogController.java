package com.omaragayev.civicflow.controller;

import com.omaragayev.civicflow.model.PermitType;
import com.omaragayev.civicflow.service.PermitCatalogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/permits/catalog")
@Tag(name = "Permit Catalog", description = "Public service catalog backed by Redis cache and RBAC controls")
public class PermitCatalogController {

    private final PermitCatalogService catalogService;

    public PermitCatalogController(PermitCatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('CITIZEN', 'OFFICER', 'ADMIN')")
    @Operation(summary = "Retrieve active permit categories", description = "Accessible by citizens, officers, and admins.")
    public ResponseEntity<List<PermitType>> getCatalog() {
        return ResponseEntity.ok(catalogService.getAllPermitTypes());
    }

    @PostMapping("/evict")
    @PreAuthorize("hasAnyRole('OFFICER', 'ADMIN')")
    @Operation(summary = "Evict permit catalog cache (Officers & Admins Only)", description = "Restricted to municipal officers and system administrators.")
    public ResponseEntity<String> evictCache() {
        catalogService.evictPermitCatalogCache();
        return ResponseEntity.ok("Permit catalog cache successfully evicted by authorized municipal officer.");
    }
}