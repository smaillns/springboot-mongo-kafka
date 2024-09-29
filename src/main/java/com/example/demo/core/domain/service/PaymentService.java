package com.example.demo.core.domain.service;


import com.example.demo.core.domain.model.Payment;
import com.example.demo.core.domain.port.api.PaymentRequester;
import com.example.demo.core.domain.port.infra.PaymentDataGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class PaymentService implements PaymentRequester {

    private final PaymentDataGateway paymentDataGateway;

    @Override
    // @Transactional(value = "mongoTransactionManager")
    public void handlePaymentReceivedEvent(Payment payment) {
        if(payment.isValid()) {
            paymentDataGateway.savePayment(payment);
        }
    }
}
