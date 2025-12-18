package com.example.freight.strategy.decorator;

import com.example.freight.enums.ExtraServiceType;
import com.example.freight.strategy.FreightStrategy;
import org.springframework.stereotype.Component;

@Component
public class SaturdayDeliveryExtraService implements ExtraServiceDecorator {

    @Override
    public ExtraServiceType supports() {
        return ExtraServiceType.SATURDAY_DELIVERY;
    }

    @Override
    public FreightStrategy apply(FreightStrategy baseStrategy) {
        return new SaturdayDeliveryDecorator(baseStrategy);
    }
}
