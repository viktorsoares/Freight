package com.example.freight;

import com.example.freight.dto.FreightRequest;
import com.example.freight.enums.CarrierType;
import com.example.freight.enums.DeliveryMode;
import com.example.freight.enums.ExtraServiceType;
import com.example.freight.strategy.impl.CorreiosStrategy;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CorreiosStrategyTest {

    private final CorreiosStrategy strategy = new CorreiosStrategy();

    private FreightRequest baseRequest(DeliveryMode mode) {
        return new FreightRequest(
                "01001000",
                "20040030",
                10.0,
                10.0,
                10.0,
                10.0,
                CarrierType.CORREIOS,
                mode,
                List.of(ExtraServiceType.INSURANCE),
                BigDecimal.ZERO,
                0.0
        );
    }

    @Test
    void shouldCalculateNormalFreight() {
        BigDecimal result = strategy.calculate(baseRequest(DeliveryMode.NORMAL), 500);
        assertThat(result).isGreaterThan(BigDecimal.ZERO);
    }

    @Test
    void shouldCalculateSedexFreight() {
        BigDecimal result = strategy.calculate(baseRequest(DeliveryMode.FAST), 500);
        assertThat(result).isGreaterThan(BigDecimal.ZERO);
    }

    @Test
    void shouldCalculateNormalDeliveryDays() {
        int days = strategy.calculateDays(500, DeliveryMode.NORMAL);
        assertThat(days).isEqualTo(4);
    }

    @Test
    void shouldCalculateSedexDeliveryDays() {
        int days = strategy.calculateDays(500, DeliveryMode.FAST);
        assertThat(days).isEqualTo(2);
    }
}
