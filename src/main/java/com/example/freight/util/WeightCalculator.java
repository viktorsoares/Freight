package com.example.freight.util;

public final class WeightCalculator {

    private WeightCalculator() {}

    public static double cubicWeight(double heightCm, double widthCm, double lengthCm) {
        if (heightCm <= 0 || widthCm <= 0 || lengthCm <= 0) {
            return 0.0;
        }
        return (heightCm * widthCm * lengthCm) / 6000.0;
    }

    public static double chargedWeight(double actualWeightKg, double cubicWeightKg) {
        return Math.max(actualWeightKg, cubicWeightKg);
    }

    public static double dimensionSum(double heightCm, double widthCm, double lengthCm) {
        return heightCm + widthCm + lengthCm;
    }

    public static double volumeLiters(double heightCm, double widthCm, double lengthCm) {
        return (heightCm * widthCm * lengthCm) / 1000.0;
    }
}
