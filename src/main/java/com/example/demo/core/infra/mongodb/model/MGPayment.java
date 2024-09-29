package com.example.demo.core.infra.mongodb.model;

import com.example.demo.core.domain.model.Payment;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Getter
@Builder
@Document(collection  = "payments")
public class MGPayment {

    @Id
    private Integer id;
    private BigDecimal amount;

    public static MGPayment fromModel(Payment payment) {
        return MGPayment.builder()
                        .id(payment.getId())
                        .amount(payment.getAmount())
                        .build();
    }

    public Payment toModel() {
        return Payment.builder()
                      .id(id)
                      .amount(amount)
                      .build();
    }

}
