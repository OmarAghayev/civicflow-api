package com.omaragayev.civicflow.controller;

import com.omaragayev.civicflow.dto.request.PermitApplicationRequest;
import com.omaragayev.civicflow.dto.request.PermitDecisionRequest;
import com.omaragayev.civicflow.dto.response.PermitApplicationResponse;
import com.omaragayev.civicflow.dto.response.PermitDecisionResponse;
import com.omaragayev.civicflow.service.AsyncVerificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/permits")
@Tag(name = "Permit Applications", description = "Public permit request submission and verification endpoints")
public class PermitController {

    private final AsyncVerificationService asyncVerificationService;

    public PermitController(AsyncVerificationService asyncVerificationService) {
        this.asyncVerificationService = asyncVerificationService;
    }

    @PostMapping("/apply")
    @PreAuthorize("hasRole('CITIZEN')")
    @Operation(summary = "Submit a digital permit application (Citizen Only)", description = "Requires ROLE_CITIZEN authentication.")
    public ResponseEntity<PermitApplicationResponse> submitApplication(
            @Valid @RequestBody PermitApplicationRequest request
    ) {
        String trackingNumber = "CV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        asyncVerificationService.processPermitVerificationAsync(
                trackingNumber,
                request.citizenPin(),
                request.permitType()
        );

        PermitApplicationResponse response = new PermitApplicationResponse(
                trackingNumber,
                "SUBMITTED",
                "Permit request submitted successfully. Background verification dispatched asynchronously.",
                LocalDateTime.now()
        );

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{trackingNumber}/decision")
    @PreAuthorize("hasAnyRole('OFFICER', 'ADMIN')")
    @Operation(summary = "Decide on permit application (Officer/Admin Only)", description = "Evaluates and approves or rejects a submitted permit application.")
    public ResponseEntity<PermitDecisionResponse> decidePermit(
            @PathVariable String trackingNumber,
            @Valid @RequestBody PermitDecisionRequest request,
            Authentication authentication
    ) {
        String officerName = (authentication != null) ? authentication.getName() : "OFFICER_SYSTEM";

        PermitDecisionResponse response = new PermitDecisionResponse(
                trackingNumber,
                request.decision(),
                officerName,
                request.officerRemarks(),
                LocalDateTime.now()
        );

        return ResponseEntity.ok(response);
    }
}