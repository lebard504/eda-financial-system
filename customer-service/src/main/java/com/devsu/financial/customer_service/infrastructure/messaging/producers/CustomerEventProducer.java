package com.devsu.financial.customer_service.infrastructure.messaging.producers;

import com.devsu.financial.customer_service.infrastructure.messaging.events.CustomerCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC = "customer.created";

    public void publish(CustomerCreatedEvent event) {
        kafkaTemplate.send(TOPIC, event);
        System.out.println("EVENT SENT: " + event);
    }
}