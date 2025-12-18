package com.example.freight.strategy.decorator;

import com.example.freight.dto.FreightRequest;
import com.example.freight.enums.CarrierType;
import com.example.freight.enums.DeliveryMode;
import com.example.freight.policy.CostPolicy;
import com.example.freight.strategy.FreightStrategy;

import java.math.BigDecimal;

public abstract class FreightDecorator implements FreightStrategy {

    protected final FreightStrategy wrappedStrategy;
    protected final CostPolicy policy;

    protected FreightDecorator(FreightStrategy wrappedStrategy, CostPolicy policy) {
        this.wrappedStrategy = wrappedStrategy;
        this.policy = policy;
    }

    @Override
    public CarrierType getCarrierType() {
        return wrappedStrategy.getCarrierType();
    }

    @Override
    public BigDecimal calculate(FreightRequest request, double distanceKm) {
        BigDecimal base = wrappedStrategy.calculate(request, distanceKm);
        return policy.apply(request, base);
    }

    @Override
    public int calculateDays(double distanceKm, DeliveryMode mode) {
        return wrappedStrategy.calculateDays(distanceKm, mode);
    }
}
