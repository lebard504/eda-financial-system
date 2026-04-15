package com.devsu.financial.customer_service.domain.repositories;

import com.devsu.financial.customer_service.domain.models.Customer;

import java.util.Optional;
import java.util.UUID;

public interface ICustomerRepository extends IBaseRepository<Customer, UUID> {
    Optional<Customer> findByIdentification(String identification);
}