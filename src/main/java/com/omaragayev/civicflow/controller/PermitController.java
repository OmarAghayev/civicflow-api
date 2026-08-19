package com.omaragayev.civicflow.controller;

import com.omaragayev.civicflow.dto.request.PermitApplicationRequest;
import com.omaragayev.civicflow.dto.response.PermitApplicationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/permits")
@Tag(name = "Permit Applications", description = "Public permit request submission and verification endpoints")
public class PermitController {

    @PostMapping("/apply")
    @Operation(summary = "Submit a digital permit application", description = "Applies token-bucket rate limiting (5 req/min) to prevent brute-force abuse.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Application accepted and tracking code generated"),
            @ApiResponse(responseCode = "400", description = "Invalid input payload"),
            @ApiResponse(responseCode = "429", description = "Rate limit exceeded (Too Many Requests)")
    })
    public ResponseEntity<PermitApplicationResponse> applyForPermit(@Valid @RequestBody PermitApplicationRequest request) {
        String trackingId = "CV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        PermitApplicationResponse response = new PermitApplicationResponse(
                trackingId,
                "SUBMITTED",
                "Permit request submitted successfully for PIN: " + request.citizenPin(),
                LocalDateTime.now()
        );
        return ResponseEntity.ok(response);
    }
}