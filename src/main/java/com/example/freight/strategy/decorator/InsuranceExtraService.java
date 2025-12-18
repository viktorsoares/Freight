package com.example.freight.strategy.decorator;

import com.example.freight.enums.ExtraServiceType;
import com.example.freight.strategy.FreightStrategy;
import org.springframework.stereotype.Component;

@Component
public class InsuranceExtraService implements ExtraServiceDecorator {

    @Override
    public ExtraServiceType supports() {
        return ExtraServiceType.INSURANCE;
    }

    @Override
    public FreightStrategy apply(FreightStrategy baseStrategy) {
        return new InsuranceDecorator(baseStrategy);
    }
}
