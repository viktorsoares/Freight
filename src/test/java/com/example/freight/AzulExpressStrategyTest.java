package com.example.freight;

import com.example.freight.dto.FreightRequest;
import com.example.freight.enums.CarrierType;
import com.example.freight.enums.DeliveryMode;
import com.example.freight.strategy.impl.AzulExpressStrategy;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AzulExpressStrategyTest {

    private final AzulExpressStrategy strategy = new AzulExpressStrategy();

    private FreightRequest request(DeliveryMode mode) {
        return new FreightRequest(
                "01001000",
                "20040030",
                3.0,
                15.0,
                15.0,
                15.0,
                CarrierType.AZUL_EXPRESS,
                mode,
                List.of(),
                BigDecimal.ZERO,
                0.0
        );
    }


    @Test
    void shouldCalculateNormalStandard() {
        BigDecimal result = strategy.calculate(request(DeliveryMode.NORMAL), 1200);
        assertThat(result).isGreaterThan(BigDecimal.ZERO);
    }

    @Test
    void shouldCalculateFastExpress() {
        BigDecimal fast = strategy.calculate(request(DeliveryMode.FAST), 1200);
        BigDecimal normal = strategy.calculate(request(DeliveryMode.NORMAL), 1200);
        assertThat(fast).isGreaterThan(normal);
    }
}
