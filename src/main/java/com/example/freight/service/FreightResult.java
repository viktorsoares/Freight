package com.example.freight.service;

import com.example.freight.enums.CarrierType;
import com.example.freight.enums.ExtraServiceType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record FreightResult(
        BigDecimal baseCost,
        BigDecimal finalCost,
        int minDeliveryDays,
        int maxDeliveryDays,
        LocalDate estimatedDeliveryDate,
        CarrierType carrier,
        List<ExtraServiceType> appliedExtras,
        double discountApplied,
        BigDecimal insuranceCost
) {}
