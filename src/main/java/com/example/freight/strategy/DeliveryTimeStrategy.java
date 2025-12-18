package com.example.freight.strategy;

import com.example.freight.enums.DeliveryMode;

public interface DeliveryTimeStrategy {

    int calculateDays(double distanceKm, DeliveryMode mode);
}
