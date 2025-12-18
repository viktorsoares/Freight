package com.example.freight.strategy;

import com.example.freight.enums.DeliveryMode;
import com.example.freight.enums.ShippingModality;

public class DefaultDeliveryTimeStrategy {

    protected int calculateDefaultDays(double distance, ShippingModality modality) {

        DeliveryMode mode = modality.getDeliveryMode();

        if (mode == DeliveryMode.FAST) {
            return distance <= 300 ? 2 : 4;
        }

        return distance <= 300 ? 5 : 8;
    }
}
