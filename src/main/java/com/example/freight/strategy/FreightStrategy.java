package com.example.freight.strategy;

import com.example.freight.dto.FreightRequest;
import com.example.freight.enums.CarrierType;
import com.example.freight.enums.DeliveryMode;

import java.math.BigDecimal;

public interface FreightStrategy {
    BigDecimal calculate(FreightRequest request, double distanceKm);
    int calculateDays(double distanceKm, DeliveryMode mode);

    CarrierType getCarrierType();
}
