package com.omaragayev.civicflow.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.math.BigDecimal;

public record PermitType(
        @Schema(example = "COMMERCIAL_LICENSE")
        String code,

        @Schema(example = "Commercial Business Operation Permit")
        String title,

        @Schema(example = "250.00")
        BigDecimal administrativeFee,

        @Schema(example = "5")
        int estimatedProcessingDays
) implements Serializable {}