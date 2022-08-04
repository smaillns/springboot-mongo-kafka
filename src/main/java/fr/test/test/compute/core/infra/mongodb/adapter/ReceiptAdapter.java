package fr.test.test.compute.core.infra.mongodb.adapter;

import fr.test.test.compute.core.domain.model.Receipt;
import fr.test.test.compute.core.domain.port.infra.ReceiptDataGateway;
import fr.test.test.compute.core.infra.mongodb.model.MGReceipt;
import fr.test.test.compute.core.infra.mongodb.respository.ReceiptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReceiptAdapter implements ReceiptDataGateway {

    private final ReceiptRepository receiptRepository;

    @Override
    public void saveReceipt(Receipt receipt) {
        receiptRepository.save(MGReceipt.fromModel(receipt));
    }

}
