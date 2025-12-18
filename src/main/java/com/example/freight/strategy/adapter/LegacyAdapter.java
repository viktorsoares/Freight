package com.example.freight.strategy.adapter;

import com.example.freight.dto.FreightRequest;
import com.example.freight.enums.CarrierType;
import com.example.freight.enums.DeliveryMode;
import com.example.freight.strategy.FreightStrategy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component("LEGACY_SYSTEM")
public class LegacyAdapter implements FreightStrategy {

    private final LegacyShippingSystem legacySystem = new LegacyShippingSystem();

    @Override
    public BigDecimal calculate(FreightRequest request, double distanceKm) {
        double miles = distanceKm * 0.621371;

        Double legacyCost = legacySystem.calculateLegacyCost(request.weight(), miles);

        return BigDecimal.valueOf(legacyCost).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    @Override
    public int calculateDays(double distanceKm, DeliveryMode mode) {
        return 0;
    }

    @Override
    public CarrierType getCarrierType() {
        return CarrierType.LEGACY_SYSTEM;
    }
}
