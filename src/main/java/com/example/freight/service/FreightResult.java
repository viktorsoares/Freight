package com.example.freight.service;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FreightResult(
        BigDecimal totalCost,
        int minDeliveryDays,
        int maxDeliveryDays,
        LocalDate deliveryDate
) {}
