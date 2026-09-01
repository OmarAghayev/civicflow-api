package com.omaragayev.civicflow.dto.response;

import java.io.Serializable;
import java.time.LocalDateTime;

public record PermitApplicationResponse(
        String trackingNumber,
        String permitType,
        String status,
        LocalDateTime createdAt
) implements Serializable {}