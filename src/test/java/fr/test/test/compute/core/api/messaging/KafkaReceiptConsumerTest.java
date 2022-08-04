package fr.test.test.compute.core.api.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.test.test.compute.common.event.ReceiptEvent;
import fr.test.test.compute.core.domain.port.api.ReceiptRequester;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import java.io.File;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@DirtiesContext
@SpringBootTest
@EmbeddedKafka(brokerProperties = {"listeners=PLAINTEXT://localhost:9092"},
               partitions = 1,
               controlledShutdown = true)
class KafkaReceiptConsumerTest {

    private static File RECEIPT_EVENT_JSON = Paths.get("src", "test", "resources", "data",
                                                                "receipt-event.json").toFile();
    @Autowired
    KafkaTemplate<String, ReceiptEvent> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${bpi.kafka.topic.receipt}")
    private String receiptTopic;


    @SpyBean
    private KafkaReceiptConsumer kafkaReceiptConsumer;

    @SpyBean
    private ReceiptRequester paymentRequester;

    @Captor
    ArgumentCaptor<ReceiptEvent> receiptEventCaptor;

    @Test
    @SneakyThrows
    void consumePaymentEvents() {

        ReceiptEvent event = objectMapper.readValue(RECEIPT_EVENT_JSON,
                                                    ReceiptEvent.class);
        kafkaTemplate.send(receiptTopic, "1", event);

        verify(kafkaReceiptConsumer, timeout(10000).times(1)).consumeReceiptEvent(
                receiptEventCaptor.capture());
        ReceiptEvent argument = receiptEventCaptor.getValue();
        Assertions.assertEquals(event.extractModel().getId(), argument.extractModel().getId());
        verify(paymentRequester, timeout(10000).times(1)).handleReceiptEvent(any());
    }

}
