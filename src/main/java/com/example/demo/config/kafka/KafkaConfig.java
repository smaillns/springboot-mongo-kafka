package com.example.demo.config.kafka;

import io.micrometer.core.instrument.MeterRegistry;
import jdk.jfr.Event;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.MicrometerConsumerListener;

@Configuration
@AllArgsConstructor
public class KafkaConfig {

    private final KafkaProperties kafkaProperties;
    private final MeterRegistry meterRegistry;

    @Bean
    @Primary
    public ConcurrentKafkaListenerContainerFactory<String, Event> kafkaListenerBatchContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Event> factory = new ConcurrentKafkaListenerContainerFactory<>();
        var defaultKafkaConsumerFactory = new DefaultKafkaConsumerFactory<>(kafkaProperties.buildConsumerProperties());
        defaultKafkaConsumerFactory.addListener(new MicrometerConsumerListener<>(meterRegistry));
        factory.setConsumerFactory(defaultKafkaConsumerFactory);
        factory.setBatchListener(true);
        return factory;
    }
}

