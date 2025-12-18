package com.example.freight;

import com.example.freight.dto.FreightRequest;
import com.example.freight.enums.CarrierType;
import com.example.freight.enums.DeliveryMode;
import com.example.freight.factory.FreightStrategyFactory;
import com.example.freight.service.DistanceService;
import com.example.freight.service.FreightResult;
import com.example.freight.service.FreightService;
import com.example.freight.strategy.impl.CorreiosStrategy;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

class FreightServiceTest {

    private final DistanceService distanceService = mock(DistanceService.class);
    private final FreightStrategyFactory factory = mock(FreightStrategyFactory.class);
    private final FreightService service = new FreightService(factory, distanceService, List.of());

    @Test
    void shouldCalculateFreightAndDeliveryTimeFast() {
        when(distanceService.calculateDistance(any(), any())).thenReturn(500.0);
        when(factory.getStrategy(CarrierType.CORREIOS)).thenReturn(new CorreiosStrategy());

        FreightRequest request = new FreightRequest(
                "01001000",
                "20040030",
                10.0,
                10.0,
                10.0,
                10.0,
                CarrierType.CORREIOS,
                DeliveryMode.FAST,
                List.of(),
                BigDecimal.ZERO,
                0.0
        );

        FreightResult result = service.calculateFreight(request);
        assertThat(result.minDeliveryDays()).isEqualTo(2);
        assertThat(result.maxDeliveryDays()).isEqualTo(2);
    }

    @Test
    void shouldCalculateFreightAndDeliveryTimeNormal() {
        when(distanceService.calculateDistance(any(), any())).thenReturn(500.0);
        when(factory.getStrategy(CarrierType.CORREIOS)).thenReturn(new CorreiosStrategy());

        FreightRequest request = new FreightRequest(
                "01001000",
                "20040030",
                10.0,
                10.0,
                10.0,
                10.0,
                CarrierType.CORREIOS,
                DeliveryMode.NORMAL,
                List.of(),
                BigDecimal.ZERO,
                0.0
        );

        FreightResult result = service.calculateFreight(request);
        assertThat(result.minDeliveryDays()).isEqualTo(4);
        assertThat(result.maxDeliveryDays()).isEqualTo(9);
    }
}
