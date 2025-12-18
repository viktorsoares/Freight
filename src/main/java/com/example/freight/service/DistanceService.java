package com.example.freight.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Service;

@Service
public class DistanceService {

    @Retry(name = "distanceService", fallbackMethod = "fallbackDistance")
    @CircuitBreaker(name = "distanceService", fallbackMethod = "fallbackDistance")
    public double calculateDistance(String originZip, String destZip) {

        try {
            int origin = Integer.parseInt(originZip);
            int dest = Integer.parseInt(destZip);

            double distance = Math.abs(dest - origin) / 10.0;

            if (distance < 1) distance = 1;
            if (distance > 5000) distance = 5000;

            return distance;

        } catch (NumberFormatException e) {
            throw new RuntimeException("CEP inválido!");
        }
    }

    public double fallbackDistance(String originZip, String destZip, Throwable t) {
        System.out.println("Circuit Breaker ativado! Usando distância padrão. Erro: " + t.getMessage());
        return 100.0;
    }
}
