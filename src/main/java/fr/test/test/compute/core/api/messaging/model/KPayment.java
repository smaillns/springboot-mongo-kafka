package fr.test.test.compute.core.api.messaging.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.test.test.compute.core.domain.model.Payment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KPayment {

    @JsonProperty("payment_id")
    private Integer paymentId;


    public Payment toModel() {
        return new Payment(paymentId);
    }

}
