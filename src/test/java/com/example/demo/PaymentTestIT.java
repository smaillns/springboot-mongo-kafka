package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.demo.common.event.PaymentEvent;
import com.example.demo.core.infra.mongodb.adapter.PaymentAdapter;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.test.annotation.DirtiesContext;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DirtiesContext
@SpringBootTest
@EmbeddedKafka(brokerProperties = {"listeners=PLAINTEXT://localhost:9092"},
        partitions = 1,
        controlledShutdown = true)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
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

    @BeforeAll
    void setUp() {
        DefaultKafkaConsumerFactory<String, String> consumerFactory = new DefaultKafkaConsumerFactory<>(
                getConsumerProperties());
        ContainerProperties containerProperties = new ContainerProperties(paymentTopic);
        container = new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);
        records = new LinkedBlockingQueue<>();
        container.setupMessageListener((MessageListener<String, String>) records::add);
        container.start();
        ContainerTestUtils.waitForAssignment(container, embeddedKafkaBroker.getPartitionsPerTopic());
    }

    @AfterAll
    void tearDown() {container.stop();}

    private Map<String, Object> getConsumerProperties() {
        return Map.of(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, embeddedKafkaBroker.getBrokersAsString(),
                ConsumerConfig.GROUP_ID_CONFIG, "consumer", ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true",
                ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "10", ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG,
                "60000", ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    }

    @Test
    void test_consume_PaymentEvent() throws IOException, InterruptedException {
        // Send Payment Event
        File EVENT_JSON = Paths.get("src", "test", "resources", "data", "payment-event.json").toFile();
        PaymentEvent sentEvent = objectMapper.readValue(EVENT_JSON, PaymentEvent.class);
        kafkaTemplate.send(paymentTopic, "1", sentEvent);

        // Check the consumed Event
        ConsumerRecord<String, String> receivedEvent = records.poll(5000, TimeUnit.MILLISECONDS);
        assertNotNull(receivedEvent);
        var result = objectMapper.readValue(receivedEvent.value(), PaymentEvent.class);
        assertEquals(928973, result.getPayload().getPaymentId());
    }
}
