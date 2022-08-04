package fr.test.test.compute.core.domain.port.infra;

import fr.test.test.compute.core.domain.model.Receipt;

public interface ReceiptDataGateway {

    void saveReceipt(Receipt receipt);
}
