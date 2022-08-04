package fr.test.test.compute.common.event;

import fr.test.test.compute.core.api.messaging.model.KReceipt;
import fr.test.test.compute.core.domain.model.Receipt;

public class ReceiptEvent extends Event<KReceipt> {

    public Receipt extractModel() {
        return this.payload.toModel();
    }
}
