package com.example.freight.enums;

public enum ShippingModality {

    SEDEX(DeliveryMode.FAST),
    PAC(DeliveryMode.NORMAL),

    PACKAGE(DeliveryMode.NORMAL),
    EXPRESS(DeliveryMode.FAST),

    CARGO(DeliveryMode.NORMAL),
    PRIORITY_CARGO(DeliveryMode.FAST),

    AZUL_NORMAL(DeliveryMode.NORMAL),
    AZUL_EXPRESS(DeliveryMode.FAST);

    private final DeliveryMode deliveryMode;

    ShippingModality(DeliveryMode deliveryMode) {
        this.deliveryMode = deliveryMode;
    }

    public DeliveryMode getDeliveryMode() {
        return deliveryMode;
    }
}

