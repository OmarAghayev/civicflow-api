package com.omaragayev.civicflow.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PermitApplicationRequest(
        @Schema(description = "Citizen National Identification PIN", example = "99AA123")
        @NotBlank(message = "Citizen PIN is mandatory")
        @Size(min = 7, max = 11, message = "PIN must be between 7 and 11 characters")
        String citizenPin,

        @Schema(description = "Type of public permit requested", example = "COMMERCIAL_BUSINESS_LICENSE")
        @NotBlank(message = "Permit type is mandatory")
        String permitType,

        @Schema(description = "Legal rationale and project description", example = "Application for launching a modern software consultancy office.")
        @NotBlank(message = "Justification text is mandatory")
        String justification
) {}
