package com.example.freight;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class FreightControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldApplyDiscountAttribute() throws Exception {
        String requestJson = """
            {
              "zipCodeOrigin": "01001000",
              "zipCodeDestination": "62875000",
              "weight": 1.5,
              "length": 10,
              "height": 100,
              "width": 10,
              "carrier": "CORREIOS",
              "deliveryMode": "NORMAL",
              "extraServices": ["INSURANCE"],
              "declaredValue": 1000.00,
              "discount": 0.10
            }
        """;

        mockMvc.perform(post("/freight/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.carrier").value("CORREIOS"))
                .andExpect(jsonPath("$.deliveryMode").value("NORMAL"))
                .andExpect(jsonPath("$.totalCost").exists())
                .andExpect(jsonPath("$.minDeliveryDays").exists())
                .andExpect(jsonPath("$.maxDeliveryDays").exists())
                .andExpect(jsonPath("$.deliveryDate").exists());
    }

    @Test
    void shouldCompareCostWithAndWithoutDiscount() throws Exception {
        String requestWithoutDiscount = """
            {
              "zipCodeOrigin": "01001000",
              "zipCodeDestination": "62875000",
              "weight": 1.5,
              "length": 10,
              "height": 100,
              "width": 10,
              "carrier": "CORREIOS",
              "deliveryMode": "NORMAL",
              "extraServices": ["INSURANCE"],
              "declaredValue": 1000.00,
              "discount": 0.0
            }
        """;

        String requestWithDiscount = """
            {
              "zipCodeOrigin": "01001000",
              "zipCodeDestination": "62875000",
              "weight": 1.5,
              "length": 10,
              "height": 100,
              "width": 10,
              "carrier": "CORREIOS",
              "deliveryMode": "NORMAL",
              "extraServices": ["INSURANCE"],
              "declaredValue": 1000.00,
              "discount": 0.10
            }
        """;

        String responseWithoutDiscount = mockMvc.perform(post("/freight/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestWithoutDiscount))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String responseWithDiscount = mockMvc.perform(post("/freight/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestWithDiscount))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        double costWithoutDiscount = JsonPath.read(responseWithoutDiscount, "$.totalCost");
        double costWithDiscount = JsonPath.read(responseWithDiscount, "$.totalCost");

        assertThat(costWithDiscount).isLessThan(costWithoutDiscount);
    }
}
