package com.example.freight.strategy.decorator;

import com.example.freight.policy.DiscountPolicy;
import com.example.freight.strategy.FreightStrategy;

public class DiscountDecorator extends FreightDecorator {
    public DiscountDecorator(FreightStrategy wrappedStrategy) {
        super(wrappedStrategy, new DiscountPolicy());
    }
}
