package com.example.freight;

import com.example.freight.service.DistanceService;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DistanceServiceTest {

    private final DistanceService service = new DistanceService();

    @Test
    void shouldUseFallbackWhenErrorOccurs() {
        double distance = service.fallbackDistance(
                "01001000",
                "20040030",
                new RuntimeException("Erro simulado")
        );
        assertThat(distance).isEqualTo(100.0);
    }
}
