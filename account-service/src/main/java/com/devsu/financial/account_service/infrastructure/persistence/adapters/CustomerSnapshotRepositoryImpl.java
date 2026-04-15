package com.devsu.financial.account_service.infrastructure.persistence.adapters;

import com.devsu.financial.account_service.domain.models.CustomerSnapshot;
import com.devsu.financial.account_service.domain.repositories.ICustomerSnapshotRepository;
import com.devsu.financial.account_service.infrastructure.entities.CustomerSnapshotEntity;
import com.devsu.financial.account_service.infrastructure.persistence.jpa.CustomerSnapshotJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CustomerSnapshotRepositoryImpl implements ICustomerSnapshotRepository {

    private final CustomerSnapshotJpaRepository jpa;

    @Override
    public CustomerSnapshot save(CustomerSnapshot snapshot) {

        CustomerSnapshotEntity entity = CustomerSnapshotEntity.builder()
                .customerId(snapshot.getCustomerId())
                .name(snapshot.getName())
                .identification(snapshot.getIdentification())
                .build();

        CustomerSnapshotEntity saved = jpa.save(entity);

        return CustomerSnapshot.builder()
                .customerId(saved.getCustomerId())
                .name(saved.getName())
                .identification(saved.getIdentification())
                .build();
    }

    @Override
    public Optional<CustomerSnapshot> findById(UUID customerId) {
        return jpa.findById(customerId)
                .map(entity -> CustomerSnapshot.builder()
                        .customerId(entity.getCustomerId())
                        .name(entity.getName())
                        .identification(entity.getIdentification())
                        .build());
    }
}