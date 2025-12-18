package com.example.freight.strategy.decorator;

import com.example.freight.dto.FreightRequest;
import com.example.freight.strategy.FreightStrategy;
import java.math.BigDecimal;

public abstract class FreightDecorator implements FreightStrategy {
    protected final FreightStrategy wrappedStrategy;

    public FreightDecorator(FreightStrategy wrappedStrategy) {
        this.wrappedStrategy = wrappedStrategy;
    }

    @Override
    public BigDecimal calculate(FreightRequest request, double distanceKm) {
        return wrappedStrategy.calculate(request, distanceKm);
    }
}
