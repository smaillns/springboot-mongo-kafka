package com.example.demo.core.api.messaging.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.example.demo.core.domain.model.Payment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KPayment {

    @JsonProperty("payment_id")
    private Integer paymentId;

    @JsonProperty("paid_amount")
    private BigDecimal amount;


    public Payment toModel() {
        return Payment.builder()
                .id(paymentId)
                .amount(amount)
                .build();
    }
}
