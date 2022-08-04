package fr.test.test.compute.core.domain.port.api;

import fr.test.test.compute.core.domain.model.Payment;

public interface PaymentRequester {
    void handlePaymentReceivedEvent(Payment payment);
}
