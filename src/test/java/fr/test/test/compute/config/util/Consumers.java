package fr.test.test.compute.config.util;

import fr.test.test.compute.common.event.PaymentEvent;
import fr.test.test.compute.config.kafka.KafkaEventDeserializer;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Configuration
public class Consumers {

    public static List<PaymentEvent> fetchPaymentEvents(String topic, EmbeddedKafkaBroker kafkaBroker) {
        ConsumerRecords<String, PaymentEvent> polledRecords = getPaymentEventConsumer(topic, kafkaBroker).poll(
                Duration.ofMillis(1000));
        List<PaymentEvent> paymentEvents = new ArrayList<>();
        polledRecords.iterator().forEachRemaining(r -> paymentEvents.add(r.value()));
        return paymentEvents;
    }

    public static Consumer<String, PaymentEvent> getPaymentEventConsumer(String topic,
                                                                         EmbeddedKafkaBroker embeddedKafka) {
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("bpi_kafka_topic_payment",
                                                                         String.valueOf(false), embeddedKafka);
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaEventDeserializer.class.getName());
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "payment-group-2");
        consumerProps.put(ConsumerConfig.CLIENT_ID_CONFIG, "payment-group-2");
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        Consumer<String, PaymentEvent> consumer = new DefaultKafkaConsumerFactory<String, PaymentEvent>(
                consumerProps).createConsumer();
        consumer.subscribe(List.of(topic));
        return consumer;
    }

}
