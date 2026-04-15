package com.devsu.financial.account_service.infrastructure.persistence.jpa;

import com.devsu.financial.account_service.infrastructure.entities.CustomerSnapshotEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CustomerSnapshotJpaRepository extends JpaRepository<CustomerSnapshotEntity, UUID> {
}