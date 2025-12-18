package com.example.freight.strategy.decorator;

import com.example.freight.dto.FreightRequest;
import com.example.freight.enums.CarrierType;
import com.example.freight.enums.DeliveryMode;
import com.example.freight.strategy.FreightStrategy;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DiscountDecorator extends FreightDecorator {

    private final double discountRate;

    public DiscountDecorator(FreightStrategy wrappedStrategy, double discountRate) {
        super(wrappedStrategy);
        this.discountRate = discountRate;
    }

    @Override
    public BigDecimal calculate(FreightRequest request, double distanceKm) {
        BigDecimal base = wrappedStrategy.calculate(request, distanceKm);
        BigDecimal discounted = base.multiply(BigDecimal.valueOf(1 - discountRate));
        return discounted.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public int calculateDays(double distanceKm, DeliveryMode mode) {
        return wrappedStrategy.calculateDays(distanceKm, mode);
    }

    @Override
    public CarrierType getCarrierType() {
        return wrappedStrategy.getCarrierType();
    }
}
