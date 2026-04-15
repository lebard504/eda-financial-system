package com.devsu.financial.account_service.infrastructure.messaging.consumers;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.devsu.financial.account_service.application.services.CustomerSnapshotService;
import com.devsu.financial.account_service.infrastructure.messaging.events.CustomerCreatedEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomerSnapshotConsumer {

    private final CustomerSnapshotService snapshotService;

    @KafkaListener(topics = "customer.created", groupId = "snapshot-group")
    public void saveSnapshot(CustomerCreatedEvent event) {
        if (event == null || event.getCustomerId() == null || event.getName() == null) {
            System.err.println("Invalid event received: " + event);
            return;
        }

        System.out.println("SNAPSHOT CONSUMER → " + event);

        try {
            snapshotService.save(event);
        } 
        catch (Exception e) {
            System.err.println("Error saving snapshot for event: " + event);
            e.printStackTrace();
        }
    }
}
