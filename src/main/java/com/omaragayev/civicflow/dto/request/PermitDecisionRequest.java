package com.omaragayev.civicflow.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record PermitDecisionRequest(
        @Schema(example = "APPROVED", allowableValues = {"APPROVED", "REJECTED"})
        @NotBlank(message = "Decision cannot be blank")
        @Pattern(regexp = "APPROVED|REJECTED", message = "Decision must be either APPROVED or REJECTED")
        String decision,

        @Schema(example = "All municipal criteria and architectural drawings verified successfully.")
        @NotBlank(message = "Officer remarks cannot be blank")
        String officerRemarks
) {}