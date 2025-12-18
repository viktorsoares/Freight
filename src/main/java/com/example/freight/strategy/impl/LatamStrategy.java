package com.example.freight.strategy.impl;

import com.example.freight.dto.FreightRequest;
import com.example.freight.enums.CarrierType;
import com.example.freight.enums.DeliveryMode;
import com.example.freight.strategy.DeliveryTimeStrategy;
import com.example.freight.strategy.FreightStrategy;
import com.example.freight.util.Money;
import com.example.freight.util.WeightCalculator;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component("LATAM")
public class LatamStrategy implements FreightStrategy, DeliveryTimeStrategy {

    private static final double LARGE_DIMENSION_THRESHOLD_CM = 100.0;
    private static final double LARGE_DIMENSION_SURCHARGE_PERCENT = 0.15;
    private static final double LARGE_VOLUME_LITERS_THRESHOLD = 150.0;
    private static final double LARGE_VOLUME_MULTIPLIER = 1.20;

    @Override
    public BigDecimal calculate(FreightRequest request, double distanceKm) {
        double cubic = WeightCalculator.cubicWeight(request.height(), request.width(), request.length());
        double charged = WeightCalculator.chargedWeight(request.weight(), cubic);

        if (request.deliveryMode() == DeliveryMode.FAST) {
            BigDecimal base = BigDecimal.valueOf(55.0);
            base = base.add(BigDecimal.valueOf(charged * 3.0));
            base = base.add(BigDecimal.valueOf((distanceKm / 100.0) * 0.45));
            base = applyDimensionalSurcharges(base, request, charged);
            return Money.round2(base);
        } else {
            double costPerKm = getCostPerKm(distanceKm);
            BigDecimal base = BigDecimal.valueOf(distanceKm * costPerKm * Math.max(1.0, charged));
            base = applyDimensionalSurcharges(base, request, charged);
            return Money.round2(base);
        }
    }

    private BigDecimal applyDimensionalSurcharges(BigDecimal base, FreightRequest request, double chargedWeight) {
        double length = request.length();
        double height = request.height();
        double width = request.width();

        if (length > LARGE_DIMENSION_THRESHOLD_CM ||
                height > LARGE_DIMENSION_THRESHOLD_CM ||
                width > LARGE_DIMENSION_THRESHOLD_CM) {
            base = base.add(base.multiply(BigDecimal.valueOf(LARGE_DIMENSION_SURCHARGE_PERCENT)));
        }

        double volumeLiters = WeightCalculator.volumeLiters(height, width, length);
        if (volumeLiters > LARGE_VOLUME_LITERS_THRESHOLD) {
            base = base.multiply(BigDecimal.valueOf(LARGE_VOLUME_MULTIPLIER));
        }

        if (chargedWeight > 50.0) {
            base = base.add(base.multiply(BigDecimal.valueOf(0.10)));
        }

        return base;
    }

    private double getCostPerKm(double km) {
        if (km <= 500) return 0.15;
        if (km <= 1000) return 0.12;
        if (km <= 1500) return 0.10;
        if (km <= 2000) return 0.09;
        if (km <= 2500) return 0.08;
        if (km <= 3000) return 0.07;
        if (km <= 3500) return 0.07;
        if (km <= 4000) return 0.06;
        if (km <= 4500) return 0.06;
        return 0.05;
    }

    @Override
    public int calculateDays(double distanceKm, DeliveryMode mode) {
        if (mode == DeliveryMode.FAST) {
            if (distanceKm <= 500) return 1;
            else if (distanceKm <= 1000) return 1;
            else if (distanceKm <= 2000) return 2;
            else if (distanceKm <= 3000) return 2;
            else if (distanceKm <= 4000) return 3;
            else if (distanceKm <= 5000) return 4;
            else return 5;
        } else {
            if (distanceKm <= 500) return 4;
            else if (distanceKm <= 1000) return 6;
            else if (distanceKm <= 1500) return 9;
            else if (distanceKm <= 2000) return 12;
            else if (distanceKm <= 2500) return 15;
            else if (distanceKm <= 3000) return 18;
            else if (distanceKm <= 3500) return 22;
            else if (distanceKm <= 4000) return 25;
            else if (distanceKm <= 4500) return 30;
            else return 35;
        }
    }

    @Override
    public CarrierType getCarrierType() {
        return CarrierType.LATAM;
    }
}
