package com.example.demo.common.event;

import com.example.demo.core.api.messaging.model.KPayment;
import com.example.demo.core.domain.model.Payment;

public class PaymentEvent extends Event<KPayment> {

    public Payment extractModel() {
        return this.payload.toModel();
    }
}
