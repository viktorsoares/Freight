package com.example.freight.factory;

import com.example.freight.enums.CarrierType;
import com.example.freight.strategy.FreightStrategy;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class FreightStrategyFactory {

    private final Map<CarrierType, FreightStrategy> strategies;

    private final Set<CarrierType> enabledCarriers = new HashSet<>(Arrays.asList(
            CarrierType.CORREIOS,
            CarrierType.JADLOG,
            CarrierType.LATAM,
            CarrierType.AZUL_EXPRESS,
            CarrierType.DEFAULT
    ));

    public FreightStrategyFactory(List<FreightStrategy> strategies) {
        this.strategies = strategies.stream()
                .collect(Collectors.toMap(
                        FreightStrategy::getCarrierType,
                        Function.identity(),
                        (existing, replacement) -> {
                            System.err.println("Duplicate strategy for carrier: "
                                    + existing.getCarrierType() + " -> mantendo a primeira");
                            return existing;
                        }
                ));
    }

    public FreightStrategy getStrategy(CarrierType carrier) {
        if (!enabledCarriers.contains(carrier)) {
            return strategies.get(CarrierType.DEFAULT);
        }
        return strategies.getOrDefault(carrier, strategies.get(CarrierType.DEFAULT));
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
