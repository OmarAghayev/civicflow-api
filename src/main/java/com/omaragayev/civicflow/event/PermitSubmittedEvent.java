package com.omaragayev.civicflow.event;

import java.time.LocalDateTime;

public record PermitSubmittedEvent(
        String trackingNumber,
        String citizenPin,
        String citizenEmail,
        String permitType,
        LocalDateTime timestamp
) {}