package com.example.demo.core.domain.port.infra;

import com.example.demo.core.domain.model.Payment;


public interface PaymentDataGateway {

    void savePayment(Payment payment);
    Payment findPaymentById(String paymentId);
}
