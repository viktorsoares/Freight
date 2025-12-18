package com.example.freight;

import com.example.freight.dto.FreightRequest;
import com.example.freight.enums.CarrierType;
import com.example.freight.enums.DeliveryMode;
import com.example.freight.enums.ExtraServiceType;
import com.example.freight.strategy.FreightStrategy;
import com.example.freight.strategy.decorator.InsuranceDecorator;
import com.example.freight.strategy.decorator.SaturdayDeliveryDecorator;
import com.example.freight.strategy.impl.CorreiosStrategy;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DecoratorsTest {

    private final FreightRequest request = new FreightRequest(
            "01001000",
            "20040030",
            0.5,    // weight
            10.0,   // length
            10.0,   // height
            10.0,   // width
            CarrierType.CORREIOS,
            DeliveryMode.NORMAL,
            List.of(ExtraServiceType.INSURANCE, ExtraServiceType.SATURDAY_DELIVERY),
            BigDecimal.valueOf(1000.00),
            0.0
    );

    @Test
    void shouldApplyInsuranceDecorator() {
        FreightStrategy base = new CorreiosStrategy();
        FreightStrategy insured = new InsuranceDecorator(base);

        BigDecimal baseCost = base.calculate(request, 500);
        BigDecimal insuredCost = insured.calculate(request, 500);

        // Seguro deve adicionar 2% do valor declarado (1000 * 0.02 = 20)
        assertThat(insuredCost).isEqualByComparingTo(baseCost.add(BigDecimal.valueOf(20.00)));
    }

    @Test
    void shouldApplySaturdayDeliveryDecorator() {
        FreightStrategy base = new CorreiosStrategy();
        FreightStrategy saturday = new SaturdayDeliveryDecorator(base);

        BigDecimal baseCost = base.calculate(request, 500);
        BigDecimal saturdayCost = saturday.calculate(request, 500);

        // Entrega no sábado adiciona R$15 fixo
        assertThat(saturdayCost).isEqualByComparingTo(baseCost.add(BigDecimal.valueOf(15.00)));
    }

    @Test
    void shouldApplyBothDecorators() {
        FreightStrategy base = new CorreiosStrategy();
        FreightStrategy insuredAndSaturday = new SaturdayDeliveryDecorator(new InsuranceDecorator(base));

        BigDecimal baseCost = base.calculate(request, 500);
        BigDecimal expected = baseCost.add(BigDecimal.valueOf(20.00)).add(BigDecimal.valueOf(15.00));
        BigDecimal result = insuredAndSaturday.calculate(request, 500);

        assertThat(result).isEqualByComparingTo(expected);
    }
}
