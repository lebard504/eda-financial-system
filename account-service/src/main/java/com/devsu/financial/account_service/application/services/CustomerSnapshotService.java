package com.devsu.financial.account_service.application.services;

import com.devsu.financial.account_service.domain.models.CustomerSnapshot;
import com.devsu.financial.account_service.domain.repositories.ICustomerSnapshotRepository;
import com.devsu.financial.account_service.infrastructure.messaging.events.CustomerCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerSnapshotService {

    private final ICustomerSnapshotRepository repository;

    public void save(CustomerCreatedEvent event) {

        CustomerSnapshot snapshot = CustomerSnapshot.builder()
                .customerId(event.getCustomerId())
                .name(event.getName())
                .identification(event.getIdentification())
                .build();

        repository.save(snapshot);
    }
}