package com.example.freight.strategy.impl;

import com.example.freight.dto.FreightRequest;
import com.example.freight.enums.CarrierType;
import com.example.freight.enums.DeliveryMode;
import com.example.freight.strategy.DeliveryTimeStrategy;
import com.example.freight.strategy.FreightStrategy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component("AZUL_EXPRESS")
public class AzulExpressStrategy implements FreightStrategy, DeliveryTimeStrategy {

    @Override
    public BigDecimal calculate(FreightRequest request, double distanceKm) {
        BigDecimal base = request.deliveryMode() == DeliveryMode.FAST
                ? BigDecimal.valueOf(60.0)
                : BigDecimal.valueOf(40.0);

        double cubicWeight = (request.height() * request.width() * request.length()) / 6000.0;
        double chargedWeight = Math.max(request.weight(), cubicWeight);

        base = base.add(BigDecimal.valueOf(chargedWeight * 3.5));

        BigDecimal distanceCost = BigDecimal.valueOf((distanceKm / 100.0) * 0.5);
        base = base.add(distanceCost);

        return base.setScale(2, RoundingMode.HALF_UP);
    }


    @Override
    public int calculateDays(double distanceKm, DeliveryMode mode) {
        if (mode == DeliveryMode.FAST) {
            if (distanceKm <= 500) return 1;
            else if (distanceKm <= 1000) return 2;
            else if (distanceKm <= 2000) return 3;
            else if (distanceKm <= 3000) return 4;
            else if (distanceKm <= 4000) return 5;
            else if (distanceKm <= 5000) return 6;
            else return 7;
        } else {
            if (distanceKm <= 500) return 3;
            else if (distanceKm <= 1000) return 4;
            else if (distanceKm <= 2000) return 6;
            else if (distanceKm <= 3000) return 8;
            else if (distanceKm <= 4000) return 10;
            else if (distanceKm <= 5000) return 12;
            else return 14;
        }
    }

    @Override
    public CarrierType getCarrierType() {
        return CarrierType.AZUL_EXPRESS;
    }
}
