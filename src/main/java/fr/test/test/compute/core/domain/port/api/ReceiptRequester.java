package fr.test.test.compute.core.domain.port.api;

import fr.test.test.compute.core.domain.model.Receipt;

public interface ReceiptRequester {
    void handleReceiptEvent(Receipt receipt);
}
