package fr.test.test.compute.config.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.test.test.compute.common.event.PaymentEvent;
import fr.test.test.compute.common.event.ReceiptEvent;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
public class KafkaEventDeserializer implements Deserializer<Object> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        Deserializer.super.configure(configs, isKey);
    }

    @Override
    public Object deserialize(String topic, byte[] bytes) {
        try {
            if (bytes == null) {
                throw new SerializationException("Can't deserialize null");
            }
            objectMapper.findAndRegisterModules();

            if (topic.equals("payment_topic")) {
                return objectMapper.readValue(new String(bytes, StandardCharsets.UTF_8), PaymentEvent.class);
            } else if (topic.equals("receipt_topic")) {
                 return objectMapper.readValue(new String(bytes, StandardCharsets.UTF_8), ReceiptEvent.class);
            }
            throw new SerializationException("Unrecognized topic");
        } catch (Exception e) {
            throw new SerializationException("Error when deserializing to Event: " + e.getLocalizedMessage());
        }
    }

    @Override
    public void close() {
        Deserializer.super.close();
    }
}
