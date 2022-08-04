package fr.test.test.compute.core.api.messaging;

import fr.test.test.compute.common.event.PaymentEvent;
import fr.test.test.compute.core.domain.port.api.PaymentRequester;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaPaymentConsumer {

    private final PaymentRequester paymentRequester;

    @KafkaListener(topics = "#{'${bpi.kafka.topic.payment}'}",
                   groupId = "#{'${bpi.kafka.group-id}'}")
    public void consumePaymentEvents(PaymentEvent paymentEvent)  {
        paymentRequester.handlePaymentReceivedEvent(paymentEvent.extractModel());
    }

}
