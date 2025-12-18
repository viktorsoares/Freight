package com.example.freight.strategy.decorator;

import com.example.freight.policy.InsurancePolicy;
import com.example.freight.strategy.FreightStrategy;

public class InsuranceDecorator extends FreightDecorator {
    public InsuranceDecorator(FreightStrategy wrappedStrategy) {
        super(wrappedStrategy, new InsurancePolicy());
    }
}
