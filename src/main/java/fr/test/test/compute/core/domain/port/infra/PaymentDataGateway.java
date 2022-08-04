package fr.test.test.compute.core.domain.port.infra;

import fr.test.test.compute.core.domain.model.Payment;


public interface PaymentDataGateway {

    void savePayment(Payment payment);
}
