package com.omaragayev.civicflow.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PermitApplicationRequest(
        @NotBlank(message = "Citizen PIN is mandatory")
        @Size(min = 5, max = 20, message = "Citizen PIN must be between 5 and 20 characters")
        String citizenPin,

        @NotBlank(message = "Applicant name is mandatory")
        @Size(min = 2, max = 100, message = "Applicant name must be between 2 and 100 characters")
        String applicantName,

        @NotBlank(message = "Email is mandatory")
        @Email(message = "Email format must be valid")
        String email,

        String phoneNumber,

        @NotBlank(message = "Permit type is mandatory")
        String permitType,

        String description
) {}