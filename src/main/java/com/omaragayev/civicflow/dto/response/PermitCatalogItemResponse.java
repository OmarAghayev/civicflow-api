package com.omaragayev.civicflow.dto.response;

import java.io.Serializable;

public record PermitCatalogItemResponse(
        String code,
        String title,
        Double feeAmount,
        Integer estimatedProcessingDays
) implements Serializable {}