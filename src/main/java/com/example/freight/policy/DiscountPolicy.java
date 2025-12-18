package com.example.freight.policy;

import com.example.freight.dto.FreightRequest;

import java.math.BigDecimal;

public class DiscountPolicy implements CostPolicy {

    @Override
    public BigDecimal apply(FreightRequest request, BigDecimal baseCost) {
        BigDecimal factor = BigDecimal.ONE.subtract(BigDecimal.valueOf(request.discount()));
        return baseCost.multiply(factor);
    }
}
