package fr.test.test.compute.core.infra.mongodb.model;

import fr.test.test.compute.core.domain.model.Payment;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;

@Getter
@Builder
public class MGPayment {

    @Id
    private Integer id;

    public static MGPayment fromModel(Payment payment) {

        return MGPayment.builder()
                        .id(payment.getId())
                        .build();

    }

    public Payment toModel() {

        return Payment.builder()
                      .id(id)
                      .build();

    }

}
