package com.example.freight.strategy.decorator;

import com.example.freight.enums.ExtraServiceType;
import com.example.freight.strategy.FreightStrategy;

public interface ExtraServiceDecorator {

    ExtraServiceType supports();

    FreightStrategy apply(FreightStrategy baseStrategy);
}
