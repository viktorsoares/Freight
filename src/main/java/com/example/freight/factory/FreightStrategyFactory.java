package com.example.freight.factory;

import com.example.freight.enums.CarrierType;
import com.example.freight.strategy.FreightStrategy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class FreightStrategyFactory {

    private final Map<CarrierType, FreightStrategy> strategies;
    private final FreightStrategy defaultStrategy;
    private final Set<CarrierType> enabledCarriers;

    public FreightStrategyFactory(List<FreightStrategy> strategies,
                                  @Qualifier("DEFAULT") FreightStrategy defaultStrategy) {
        this.defaultStrategy = defaultStrategy;
        this.enabledCarriers = new LinkedHashSet<>(Arrays.asList(
                CarrierType.CORREIOS,
                CarrierType.JADLOG,
                CarrierType.LATAM,
                CarrierType.AZUL_EXPRESS
        ));
        this.strategies = strategies.stream()
                .collect(Collectors.toMap(
                        FreightStrategy::getCarrierType,
                        Function.identity(),
                        (existing, replacement) -> existing
                ));
    }

    public FreightStrategy getStrategy(CarrierType carrier) {
        if (!enabledCarriers.contains(carrier)) {
            return defaultStrategy;
        }
        return strategies.getOrDefault(carrier, defaultStrategy);
    }

    public void enableCarrier(CarrierType carrier) {
        enabledCarriers.add(carrier);
    }

    public void disableCarrier(CarrierType carrier) {
        enabledCarriers.remove(carrier);
    }

    public boolean isCarrierEnabled(CarrierType carrier) {
        return enabledCarriers.contains(carrier);
    }
}
