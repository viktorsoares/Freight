package com.example.freight.strategy.adapter;

public class LegacyShippingSystem {
    // Método antigo retorna Double e recebe parâmetros soltos
    public Double calculateLegacyCost(double kilos, double miles) {
        return (kilos * 10.0) + (miles * 0.5);
    }
}
