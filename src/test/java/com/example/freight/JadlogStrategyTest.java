package com.example.freight;

import com.example.freight.dto.FreightRequest;
import com.example.freight.enums.CarrierType;
import com.example.freight.enums.DeliveryMode;
import com.example.freight.strategy.impl.JadlogStrategy;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JadlogStrategyTest {

    private final JadlogStrategy strategy = new JadlogStrategy();

    private FreightRequest request(DeliveryMode mode) {
        return new FreightRequest(
                "01001000",
                "20040030",
                8.0,
                40.0,
                40.0,
                40.0,
                CarrierType.JADLOG,
                mode,
                List.of(),
                BigDecimal.ZERO,
                0.0
        );
    }

    @Test
    void shouldCalculateNormalPackage() {
        BigDecimal result = strategy.calculate(request(DeliveryMode.NORMAL), 400);
        assertThat(result).isGreaterThan(BigDecimal.ZERO);
    }

    @Test
    void shouldCalculateFastPackage() {
        BigDecimal fast = strategy.calculate(request(DeliveryMode.FAST), 400);
        BigDecimal normal = strategy.calculate(request(DeliveryMode.NORMAL), 400);
        assertThat(fast).isGreaterThan(normal);
    }
}
