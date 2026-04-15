package com.devsu.financial.account_service.domain.repositories;

import com.devsu.financial.account_service.domain.models.CustomerSnapshot;

import java.util.Optional;
import java.util.UUID;

public interface ICustomerSnapshotRepository {

    CustomerSnapshot save(CustomerSnapshot snapshot);

    Optional<CustomerSnapshot> findById(UUID customerId);
}