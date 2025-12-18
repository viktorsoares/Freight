package com.example.freight.strategy.decorator;

import com.example.freight.dto.FreightRequest;
import com.example.freight.enums.CarrierType;
import com.example.freight.enums.DeliveryMode;
import com.example.freight.strategy.FreightStrategy;

import java.math.BigDecimal;

public class SaturdayDeliveryDecorator extends FreightDecorator {

    public SaturdayDeliveryDecorator(FreightStrategy wrappedStrategy) {
        super(wrappedStrategy);
    }

    @Override
    public BigDecimal calculate(FreightRequest request, double distanceKm) {
        BigDecimal baseCost = super.calculate(request, distanceKm);
        return baseCost.add(new BigDecimal("15.00")).setScale(2, BigDecimal.ROUND_HALF_UP);
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
