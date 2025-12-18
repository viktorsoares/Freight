package com.example.freight.service;

import com.example.freight.dto.FreightRequest;
import com.example.freight.enums.DeliveryMode;
import com.example.freight.enums.ExtraServiceType;
import com.example.freight.factory.FreightStrategyFactory;
import com.example.freight.strategy.DeliveryTimeStrategy;
import com.example.freight.strategy.FreightStrategy;
import com.example.freight.strategy.decorator.DiscountDecorator;
import com.example.freight.strategy.decorator.InsuranceDecorator;
import com.example.freight.strategy.decorator.SaturdayDeliveryDecorator;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
@Slf4j
public class FreightService {

    private final FreightStrategyFactory strategyFactory;
    private final DistanceService distanceService;

    private static final double INSURANCE_RATE = 0.02;

    @Timed(value = "freight.calculate.time", description = "Tempo de cálculo de frete")
    public FreightResult calculateFreight(FreightRequest request) {
        log.info("Calculando frete: origem={}, destino={}, carrier={}, peso={}kg, desconto={}%",
                request.zipCodeOrigin(), request.zipCodeDestination(),
                request.carrier(), request.weight(), request.discount() * 100);

        double distanceKm = distanceService.calculateDistance(
                request.zipCodeOrigin(),
                request.zipCodeDestination()
        );

        FreightStrategy baseStrategy = strategyFactory.getStrategy(request.carrier());

        FreightStrategy costStrategy = baseStrategy;
        List<ExtraServiceType> extras = request.extraServices();
        if (extras != null) {
            if (extras.contains(ExtraServiceType.INSURANCE)) {
                costStrategy = new InsuranceDecorator(costStrategy);
            }
            if (extras.contains(ExtraServiceType.SATURDAY_DELIVERY)) {
                costStrategy = new SaturdayDeliveryDecorator(costStrategy);
            }
        }
        if (request.discount() > 0.0) {
            costStrategy = new DiscountDecorator(costStrategy);
        }

        BigDecimal baseCost = strategyFactory.getStrategy(request.carrier())
                .calculate(request, distanceKm);
        BigDecimal finalCost = costStrategy.calculate(request, distanceKm);

        baseCost = baseCost.setScale(2, RoundingMode.HALF_UP);
        finalCost = finalCost.setScale(2, RoundingMode.HALF_UP);

        BigDecimal insuranceCost = BigDecimal.ZERO;
        if (extras != null && extras.contains(ExtraServiceType.INSURANCE)) {
            insuranceCost = calculateInsuranceAmount(request);
            insuranceCost = insuranceCost.setScale(2, RoundingMode.HALF_UP);
        }

        boolean sameCep = request.zipCodeOrigin() != null &&
                request.zipCodeOrigin().equals(request.zipCodeDestination());
        int baseDays;
        if (sameCep) {
            baseDays = 1;
        } else {
            if (baseStrategy instanceof DeliveryTimeStrategy timeStrategy) {
                DeliveryMode effectiveMode = request.deliveryMode() != null ? request.deliveryMode() : DeliveryMode.NORMAL;
                baseDays = timeStrategy.calculateDays(distanceKm, effectiveMode);
            } else {
                baseDays = 1;
            }
        }

        baseDays = Math.max(baseDays, 1);

        int minDays;
        int maxDays;
        if (request.deliveryMode() == DeliveryMode.FAST) {
            minDays = maxDays = baseDays;
        } else {
            minDays = baseDays;
            maxDays = baseDays + 7;
        }

        LocalDate deliveryDate = pickBusinessDayBetween(LocalDate.now(), minDays, maxDays, extras != null && extras.contains(ExtraServiceType.SATURDAY_DELIVERY));

        return new FreightResult(
                baseCost,
                finalCost,
                minDays,
                maxDays,
                deliveryDate,
                request.carrier(),
                request.extraServices(),
                request.discount(),
                insuranceCost
        );
    }

    private BigDecimal calculateInsuranceAmount(FreightRequest request) {
        double declared = request.declaredValue() != null ? request.declaredValue().doubleValue() : 0.0;
        double amount = declared * INSURANCE_RATE;
        double minimum = 9.0;
        if (amount < minimum && declared > 0.0) {
            amount = minimum;
        }
        return BigDecimal.valueOf(amount);
    }

    private LocalDate pickBusinessDayBetween(LocalDate startDate, int minDays, int maxDays, boolean allowSaturday) {
        if (minDays > maxDays) {
            int tmp = minDays;
            minDays = maxDays;
            maxDays = tmp;
        }

        int chosenOffset = ThreadLocalRandom.current().nextInt(minDays, maxDays + 1);
        return addBusinessDays(startDate, chosenOffset, allowSaturday);
    }

    private LocalDate addBusinessDays(LocalDate startDate, int days, boolean allowSaturday) {
        LocalDate date = startDate;
        int added = 0;
        while (added < days) {
            date = date.plusDays(1);
            DayOfWeek dow = date.getDayOfWeek();
            boolean isWeekend = (dow == DayOfWeek.SUNDAY) || (dow == DayOfWeek.SATURDAY && !allowSaturday);
            if (!isWeekend) {
                added++;
            }
        }
        return date;
    }

    private LocalDate addBusinessDays(LocalDate startDate, int days) {
        return addBusinessDays(startDate, days, false);
    }
}
