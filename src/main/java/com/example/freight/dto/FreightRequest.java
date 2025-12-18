package com.example.freight.dto;

import com.example.freight.enums.CarrierType;
import com.example.freight.enums.DeliveryMode;
import com.example.freight.enums.ExtraServiceType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;

public record FreightRequest(

        @NotBlank
        @Pattern(regexp = "\\d{8}")
        String zipCodeOrigin,

        @NotBlank
        @Pattern(regexp = "\\d{8}")
        String zipCodeDestination,

        @Positive double weight,
        @Positive double length,
        @Positive double height,
        @Positive double width,

        @NotNull CarrierType carrier,

        @NotNull DeliveryMode deliveryMode,

        List<ExtraServiceType> extraServices,

        @DecimalMin("0.0") BigDecimal declaredValue,
        @DecimalMin("0.0") double discount
) {}
