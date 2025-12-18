package com.example.freight.policy;

import com.example.freight.dto.FreightRequest;

import java.math.BigDecimal;

public interface CostPolicy {
    BigDecimal apply(FreightRequest request, BigDecimal baseCost);
}
