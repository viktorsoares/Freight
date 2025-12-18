package com.example.freight.service;

import com.example.freight.dto.FreightRequest;
import com.example.freight.enums.DeliveryMode;
import com.example.freight.enums.ExtraServiceType;
import com.example.freight.factory.FreightStrategyFactory;
import com.example.freight.strategy.DeliveryTimeStrategy;
import com.example.freight.strategy.FreightStrategy;
import com.example.freight.strategy.decorator.ExtraServiceDecorator;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FreightService {

    private final FreightStrategyFactory strategyFactory;
    private final DistanceService distanceService;
    private final List<ExtraServiceDecorator> extraServiceDecorators;

    @Timed(value = "freight.calculate.time", description = "Tempo de cálculo de frete")
    public FreightResult calculateFreight(FreightRequest request) {
        log.info("Calculando frete: origem={}, destino={}, carrier={}, peso={}kg, desconto={}%",
                request.zipCodeOrigin(), request.zipCodeDestination(),
                request.carrier(), request.weight(), request.discount() * 100);

        double distance = distanceService.calculateDistance(
                request.zipCodeOrigin(),
                request.zipCodeDestination()
        );

        FreightStrategy baseStrategy =
                request.carrier() != null
                        ? strategyFactory.getStrategy(request.carrier())
                        : strategyFactory.getStrategy(null);

        FreightStrategy strategy = baseStrategy;

        if (request.extraServices() != null) {
            for (ExtraServiceType extra : request.extraServices()) {
                FreightStrategy current = strategy;

                strategy = extraServiceDecorators.stream()
                        .filter(d -> d.supports() == extra)
                        .findFirst()
                        .map(d -> d.apply(current))
                        .orElse(current);
            }
        }

        BigDecimal cost = strategy.calculate(request, distance)
                .setScale(2, RoundingMode.HALF_UP);

        if (request.discount() > 0.0) {
            cost = cost.multiply(BigDecimal.valueOf(1 - request.discount()))
                    .setScale(2, RoundingMode.HALF_UP);
        }

        int baseDays = (baseStrategy instanceof DeliveryTimeStrategy timeStrategy)
                ? timeStrategy.calculateDays(
                distance,
                request.deliveryMode() != null
                        ? request.deliveryMode()
                        : DeliveryMode.NORMAL
        )
                : 0;

        int minDays;
        int maxDays;
        LocalDate deliveryDate;

        if (request.deliveryMode() == DeliveryMode.FAST) {
            minDays = baseDays;
            maxDays = baseDays;
            deliveryDate = addBusinessDays(LocalDate.now(), baseDays);
        } else {
            minDays = baseDays;
            maxDays = baseDays + 5;
            deliveryDate = addBusinessDays(LocalDate.now(), maxDays);
        }

        return new FreightResult(cost, minDays, maxDays, deliveryDate);
    }

    private LocalDate addBusinessDays(LocalDate startDate, int days) {
        LocalDate date = startDate;
        int added = 0;

        while (added < days) {
            date = date.plusDays(1);
            if (date.getDayOfWeek() != DayOfWeek.SATURDAY &&
                    date.getDayOfWeek() != DayOfWeek.SUNDAY) {
                added++;
            }
        }
        return date;
    }
}
