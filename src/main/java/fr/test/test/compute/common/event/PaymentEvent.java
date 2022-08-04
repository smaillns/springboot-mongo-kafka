package fr.test.test.compute.common.event;

import fr.test.test.compute.core.api.messaging.model.KPayment;
import fr.test.test.compute.core.domain.model.Payment;

public class PaymentEvent extends Event<KPayment> {

    public Payment extractModel() {
        return this.payload.toModel();
    }
}
