package com.example.freight.strategy.decorator;

import com.example.freight.dto.FreightRequest;
import com.example.freight.enums.CarrierType;
import com.example.freight.enums.DeliveryMode;
import com.example.freight.strategy.FreightStrategy;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class InsuranceDecorator extends FreightDecorator {

    private final BigDecimal insuranceRate = new BigDecimal("0.02");

    public InsuranceDecorator(FreightStrategy wrappedStrategy) {
        super(wrappedStrategy);
    }

    @Override
    public BigDecimal calculate(FreightRequest request, double distanceKm) {
        BigDecimal baseCost = super.calculate(request, distanceKm);

        if (request.declaredValue() != null && request.declaredValue().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal insuranceCost = request.declaredValue().multiply(insuranceRate);
            return baseCost.add(insuranceCost).setScale(2, RoundingMode.HALF_UP);
        }

        return baseCost;
    }

    @Override
    public int calculateDays(double distanceKm, DeliveryMode mode) {
        return 0;
    }

    @Override
    public CarrierType getCarrierType() {
        return null;
    }
}

