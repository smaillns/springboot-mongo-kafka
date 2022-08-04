package fr.test.test.compute.core.domain.service;


import fr.test.test.compute.core.domain.model.Receipt;
import fr.test.test.compute.core.domain.port.api.ReceiptRequester;
import fr.test.test.compute.core.domain.port.infra.ReceiptDataGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReceiptService implements ReceiptRequester {

    private final ReceiptDataGateway receiptDataGateway;

    @Override
    public void handleReceiptEvent(Receipt receipt) {
        receiptDataGateway.saveReceipt(receipt);
    }
}
