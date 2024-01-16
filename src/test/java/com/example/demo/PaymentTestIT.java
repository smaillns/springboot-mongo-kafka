package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.demo.common.event.PaymentEvent;
import com.example.demo.core.infra.mongodb.adapter.PaymentAdapter;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.BlockingQueue;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@DirtiesContext
@SpringBootTest
@EmbeddedKafka(brokerProperties = {"listeners=PLAINTEXT://localhost:9092"},
        partitions = 1,
        controlledShutdown = true)
public class PaymentTestIT {

    @Autowired
    KafkaTemplate<String, PaymentEvent> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${demo.kafka.topic.payment}")
    private String paymentTopic;


    @Autowired
    private PaymentAdapter paymentAdapter;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    private BlockingQueue<ConsumerRecord<String, String>> records;

    private KafkaMessageListenerContainer<String, String> container;

    @Test
    void test_consume_PaymentEvent() throws IOException, InterruptedException {
        // Send Payment Event
        File EVENT_JSON = Paths.get("src", "test", "resources", "data", "payment-event.json").toFile();
        PaymentEvent sentEvent = objectMapper.readValue(EVENT_JSON, PaymentEvent.class);
        kafkaTemplate.send(paymentTopic, "1", sentEvent);


        // More .. check if the received event was saved properly
//         var savedPayment = paymentAdapter.findPaymentById(sentEvent.getPayload().getPaymentId().toString());
//         assertEquals(sentEvent.getPayload().getPaymentId(), savedPayment.getId());
    }



}
