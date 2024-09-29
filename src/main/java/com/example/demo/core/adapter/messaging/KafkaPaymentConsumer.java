package com.example.demo.core.adapter.messaging;

import com.example.demo.core.domain.service.PaymentRequester;
import com.example.demo.common.event.PaymentEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaPaymentConsumer {

    private final PaymentRequester paymentRequester;

    @KafkaListener(topics = "#{'${demo.kafka.topic.payment}'}", groupId = "#{'${demo.kafka.group-id}'}")
    public void consumePaymentEvents(PaymentEvent paymentEvent)  {
        paymentRequester.handlePaymentReceivedEvent(paymentEvent.extractModel());
    }

}
