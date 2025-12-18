package com.example.freight.strategy.impl;

import com.example.freight.dto.FreightRequest;
import com.example.freight.enums.CarrierType;
import com.example.freight.enums.DeliveryMode;
import com.example.freight.strategy.DeliveryTimeStrategy;
import com.example.freight.strategy.FreightStrategy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component("DEFAULT")
public class DefaultFreightStrategy implements FreightStrategy, DeliveryTimeStrategy {

    @Override
    public BigDecimal calculate(FreightRequest request, double distanceKm) {

        double cubicWeight = (request.height() * request.width() * request.length()) / 6000.0;
        double chargedWeight = Math.max(request.weight(), cubicWeight);

        BigDecimal weightCost = BigDecimal.valueOf(chargedWeight * 1.5);
        BigDecimal distanceCost = BigDecimal.valueOf(distanceKm / 100.0 * 2.0);

        BigDecimal base = weightCost.add(distanceCost);

        return base.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public int calculateDays(double distanceKm, DeliveryMode mode) {
        if (distanceKm <= 100) return 2;
        if (distanceKm <= 500) return 4;
        if (distanceKm <= 1500) return 6;
        return 8;
    }

    @Override
    public CarrierType getCarrierType() {
        return CarrierType.DEFAULT;
    }
}
