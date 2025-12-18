package com.example.freight.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FreightResponse(
        BigDecimal totalCost,
        String carrier,
        String deliveryMode,
        int minDeliveryDays,
        int maxDeliveryDays,
        LocalDate deliveryDate,
        String message
) {}
