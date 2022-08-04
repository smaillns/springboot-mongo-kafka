package fr.test.test.compute;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.test.test.compute.common.event.PaymentEvent;
import fr.test.test.compute.core.infra.mongodb.adapter.PaymentAdapter;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
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

    @Value("${bpi.kafka.topic.payment}")
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

    private Map<String, Object> getConsumerProperties() {
        return Map.of(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, embeddedKafkaBroker.getBrokersAsString(),
                      ConsumerConfig.GROUP_ID_CONFIG, "consumer", ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true",
                      ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "10", ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG,
                      "60000", ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                      ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                      ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    }

    @Test
    void test_calculateOverdue_onDisbursementAvailableEvent() throws IOException, InterruptedException {
        // Send Payment Event
        File EVENT_JSON = Paths.get("src", "test", "resources", "data", "payment-event.json")
                                                .toFile();
        PaymentEvent sentEvent = objectMapper.readValue(EVENT_JSON,
                                                        PaymentEvent.class);
        kafkaTemplate.send(paymentTopic, "1", sentEvent);

        // // Verify that an Overdue Event was published
        // ConsumerRecord<String, String> overdueMessage = records.poll(1000, TimeUnit.MILLISECONDS);
        // assertNotNull(overdueMessage);
        // OverdueDisbursementDetectedEvent result = objectMapper.readValue(overdueMessage.value(),
        //                                                                  OverdueDisbursementDetectedEvent.class);
        // assertNotNull(result);
        // assertEquals(sentEvent.getPayload().getContractRef(), result.getPayload().getData().getContractRef());

        // // Check the persisted Overdue
        // var mgOverdueDisbursement = disbursementOverdueRepository.findAll().get(0); //  findById doesn't work !!
        // assertNotNull(mgOverdueDisbursement);
        // assertEquals(sentEvent.getPayload().getSigCode(), mgOverdueDisbursement.getSigCode());
        // assertEquals("01D00458701", mgOverdueDisbursement.getContractRef());
        // assertEquals(new BigDecimal("8288.60"), mgOverdueDisbursement.getOverdueTTC().getAmount());

        // More .. check if the received disbursement was saved properly
        // var savedDisbursement = paymentAdapter.findDisbursementById(sentEvent.getPayload().getDisbursementId());
        // assertEquals(sentEvent.getPayload().getDisbursementId(), savedDisbursement.getId());
        // assertTrue(savedDisbursement.isOverdue());
    }

    @Test
    void test_calculateOverdue_forNonDue_DisbursementReceivedEvent()  {

    }
}
