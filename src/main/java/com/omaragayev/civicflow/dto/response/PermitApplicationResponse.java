package com.omaragayev.civicflow.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

public record PermitApplicationResponse(
        @Schema(example = "CV-2026-98124")
        String trackingNumber,

        @Schema(example = "SUBMITTED")
        String status,

        @Schema(example = "Permit request submitted successfully and queued for official verification.")
        String message,

        LocalDateTime submissionTime
) {}