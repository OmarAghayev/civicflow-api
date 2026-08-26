package com.omaragayev.civicflow.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

public record PermitDecisionResponse(
        @Schema(example = "CV-25587B82")
        String trackingNumber,

        @Schema(example = "APPROVED")
        String decisionStatus,

        @Schema(example = "OFFICER_99")
        String evaluatedBy,

        @Schema(example = "All municipal criteria verified successfully.")
        String remarks,

        LocalDateTime decisionTimestamp
) {}