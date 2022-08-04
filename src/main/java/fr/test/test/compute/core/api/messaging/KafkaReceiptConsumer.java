package fr.test.test.compute.core.api.messaging;

import fr.test.test.compute.common.event.ReceiptEvent;
import fr.test.test.compute.core.domain.port.api.ReceiptRequester;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaReceiptConsumer {

    private final ReceiptRequester receiptRequester;

    @KafkaListener(topics = "#{'${bpi.kafka.topic.receipt}'}",
                   groupId = "#{'${bpi.kafka.group-id}'}")
    public void consumeReceiptEvent(ReceiptEvent receiptEvent)  {
        receiptRequester.handleReceiptEvent(receiptEvent.extractModel());
    }

}
