package com.example.freight;

import com.example.freight.dto.FreightRequest;
import com.example.freight.enums.CarrierType;
import com.example.freight.enums.DeliveryMode;
import com.example.freight.strategy.impl.LatamStrategy;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LatamStrategyTest {

    private final LatamStrategy strategy = new LatamStrategy();

    private FreightRequest request(DeliveryMode mode) {
        return new FreightRequest(
                "01001000",
                "20040030",
                5.0,
                20.0,
                20.0,
                20.0,
                CarrierType.LATAM,
                mode,
                List.of(),
                BigDecimal.ZERO,
                0.0
        );
    }

    @Test
    void shouldCalculateNormalStandard() {
        BigDecimal result = strategy.calculate(request(DeliveryMode.NORMAL), 800);
        assertThat(result).isGreaterThan(BigDecimal.ZERO);
    }

    @Test
    void shouldCalculateFastExpress() {
        BigDecimal fast = strategy.calculate(request(DeliveryMode.FAST), 800);
        BigDecimal normal = strategy.calculate(request(DeliveryMode.NORMAL), 800);
        assertThat(fast).isGreaterThan(normal);
    }
}
