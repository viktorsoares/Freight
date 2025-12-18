package com.example.freight;

import com.example.freight.dto.FreightRequest;
import com.example.freight.enums.CarrierType;
import com.example.freight.enums.DeliveryMode;
import com.example.freight.strategy.impl.DefaultFreightStrategy;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultFreightStrategyTest {

    private final DefaultFreightStrategy strategy = new DefaultFreightStrategy();

    private FreightRequest request() {
        return new FreightRequest(
                "01001000",
                "20040030",
                2.0,    // weight
                10.0,   // length
                10.0,   // height
                10.0,   // width
                CarrierType.LEGACY_SYSTEM,
                DeliveryMode.NORMAL,
                List.of(),
                BigDecimal.ZERO,
                0.0
        );
    }

    @Test
    void shouldCalculateCost() {
        BigDecimal result = strategy.calculate(request(), 600);
        assertThat(result).isGreaterThan(BigDecimal.ZERO);
    }

    @Test
    void shouldCalculateDays() {
        int days = strategy.calculateDays(600, DeliveryMode.NORMAL);
        assertThat(days).isEqualTo(4);
    }
}
