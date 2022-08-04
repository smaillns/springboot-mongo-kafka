package fr.test.test.compute.core.api.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.test.test.compute.common.event.PaymentEvent;
import fr.test.test.compute.core.domain.port.api.PaymentRequester;
import lombok.SneakyThrows;
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
class KafkaPaymentConsumerTest {

    private static File PAYMENT_EVENT_JSON = Paths.get("src", "test", "resources", "data",
                                                                "payment-event.json").toFile();
    @Autowired
    KafkaTemplate<String, PaymentEvent> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${bpi.kafka.topic.payment}")
    private String paymentTopic;


    @SpyBean
    private KafkaPaymentConsumer kafkaPaymentConsumer;

    @SpyBean
    private PaymentRequester paymentRequester;

    @Captor
    ArgumentCaptor<PaymentEvent> paymentEventCaptor;

    @Test
    @SneakyThrows
    void consumePaymentEvents() {

        PaymentEvent event = objectMapper.readValue(PAYMENT_EVENT_JSON,
                                                         PaymentEvent.class);
        kafkaTemplate.send(paymentTopic, "1", event);

        verify(kafkaPaymentConsumer, timeout(10000).times(1)).consumePaymentEvents(
                paymentEventCaptor.capture());
        PaymentEvent argument = paymentEventCaptor.getValue();
        assertEquals(event.extractModel().getId(), argument.extractModel().getId());
        verify(paymentRequester, timeout(10000).times(1)).handlePaymentReceivedEvent(any());
    }

}
