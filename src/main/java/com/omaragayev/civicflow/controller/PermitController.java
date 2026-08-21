package com.omaragayev.civicflow.controller;

import com.omaragayev.civicflow.dto.request.PermitApplicationRequest;
import com.omaragayev.civicflow.dto.response.PermitApplicationResponse;
import com.omaragayev.civicflow.service.AsyncVerificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/permits")
@Tag(name = "Permit Applications", description = "Public permit request submission and verification endpoints")
public class PermitController {

    private static final Logger log = LoggerFactory.getLogger(PermitController.class);
    private final AsyncVerificationService verificationService;

    public PermitController(AsyncVerificationService verificationService) {
        this.verificationService = verificationService;
    }

    @PostMapping("/apply")
    @Operation(summary = "Submit a digital permit application", description = "Applies token-bucket rate limiting and dispatches asynchronous background verification.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Application accepted and dispatched for async processing"),
            @ApiResponse(responseCode = "400", description = "Invalid input payload"),
            @ApiResponse(responseCode = "429", description = "Rate limit exceeded (Too Many Requests)")
    })
    public ResponseEntity<PermitApplicationResponse> applyForPermit(@Valid @RequestBody PermitApplicationRequest request) {
        String trackingId = "CV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        log.info("[HTTP INCOMING] Received application for PIN: {} on thread: {}", 
                request.citizenPin(), Thread.currentThread().getName());

        // Dispatch background verification asynchronously to dedicated worker pool
        verificationService.performBackgroundVerification(trackingId, request.citizenPin());

        PermitApplicationResponse response = new PermitApplicationResponse(
                trackingId,
                "SUBMITTED",
                "Permit request submitted successfully. Background verification dispatched asynchronously.",
                LocalDateTime.now()
        );

        log.info("[HTTP OUTGOING] Immediate response dispatched for tracking: {} on thread: {}", 
                trackingId, Thread.currentThread().getName());

        return ResponseEntity.ok(response);
    }
}