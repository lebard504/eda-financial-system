package com.devsu.financial.account_service.infrastructure.messaging.producers;

import com.devsu.financial.account_service.infrastructure.messaging.events.TransactionCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionEventProducer {
    private final KafkaTemplate<String, TransactionCreatedEvent> kafkaTemplate;

    private static final String TOPIC = "transaction.created";

    public void publish(TransactionCreatedEvent event) {
        kafkaTemplate.send(TOPIC, event.getTransactionId().toString(), event);
    }
}