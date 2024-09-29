package com.example.demo.core.domain.service;

import com.example.demo.core.domain.model.Payment;

public interface PaymentRequester {
    void handlePaymentReceivedEvent(Payment payment);
}
