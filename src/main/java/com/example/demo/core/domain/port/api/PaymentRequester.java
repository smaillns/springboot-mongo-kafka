package com.example.demo.core.domain.port.api;

import com.example.demo.core.domain.model.Payment;

public interface PaymentRequester {
    void handlePaymentReceivedEvent(Payment payment);
}
