package com.devsu.financial.customer_service.infrastructure.persistence.jpa;

import com.devsu.financial.customer_service.infrastructure.entities.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CustomerJpaRepository extends JpaRepository<CustomerEntity, UUID> {
    Optional<CustomerEntity> findByIdentification(String identification);
}