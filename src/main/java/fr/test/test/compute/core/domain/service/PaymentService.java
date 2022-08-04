package fr.test.test.compute.core.domain.service;


import fr.test.test.compute.core.domain.model.Payment;
import fr.test.test.compute.core.domain.port.api.PaymentRequester;
import fr.test.test.compute.core.domain.port.infra.PaymentDataGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class PaymentService implements PaymentRequester {

    private final PaymentDataGateway paymentDataGateway;

    @Override
    @Transactional(value = "mongoTransactionManager")
    public void handlePaymentReceivedEvent(Payment payment) {
        paymentDataGateway.savePayment(payment);
    }
}
