package com.example.freight.policy;

import com.example.freight.dto.FreightRequest;

import java.math.BigDecimal;

public class InsurancePolicy implements CostPolicy {
    private final BigDecimal rate;

    public InsurancePolicy() {
        this.rate = new BigDecimal("0.02");
    }

    public InsurancePolicy(BigDecimal rate) {
        this.rate = rate;
    }

    @Override
    public BigDecimal apply(FreightRequest request, BigDecimal baseCost) {
        return baseCost.add(request.declaredValue().multiply(rate));
    }
}
