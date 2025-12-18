package com.example.freight.policy;

import com.example.freight.dto.FreightRequest;

import java.math.BigDecimal;

public class SaturdayDeliveryPolicy implements CostPolicy {
    private final BigDecimal fixed;

    public SaturdayDeliveryPolicy() {
        this.fixed = new BigDecimal("15.00");
    }

    public SaturdayDeliveryPolicy(BigDecimal fixed) {
        this.fixed = fixed;
    }

    @Override
    public BigDecimal apply(FreightRequest request, BigDecimal baseCost) {
        return baseCost.add(fixed);
    }
}
