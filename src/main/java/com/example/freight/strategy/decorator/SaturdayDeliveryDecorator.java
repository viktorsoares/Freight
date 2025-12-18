package com.example.freight.strategy.decorator;

import com.example.freight.policy.SaturdayDeliveryPolicy;
import com.example.freight.strategy.FreightStrategy;

public class SaturdayDeliveryDecorator extends FreightDecorator {
    public SaturdayDeliveryDecorator(FreightStrategy wrappedStrategy) {
        super(wrappedStrategy, new SaturdayDeliveryPolicy());
    }
}
