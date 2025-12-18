package com.example.freight.controller;

import com.example.freight.dto.FreightRequest;
import com.example.freight.dto.FreightResponse;
import com.example.freight.service.FreightResult;
import com.example.freight.service.FreightService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/freight")
public class FreightController {

    private final FreightService freightService;

    public FreightController(FreightService freightService) {
        this.freightService = freightService;
    }

    @PostMapping("/calculate")
    public ResponseEntity<FreightResponse> calculate(
            @RequestBody @Valid FreightRequest request
    ) {

        FreightResult result = freightService.calculateFreight(request);

        return ResponseEntity.ok(new FreightResponse(
                result.totalCost(),
                request.carrier() != null ? request.carrier().name() : "DEFAULT",
                request.deliveryMode() != null ? request.deliveryMode().name() : "NORMAL",
                result.minDeliveryDays(),
                result.maxDeliveryDays(),
                result.deliveryDate(),
                "Cálculo realizado com sucesso"
        ));
    }
}
