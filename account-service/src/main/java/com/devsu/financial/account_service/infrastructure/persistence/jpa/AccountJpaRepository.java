package com.devsu.financial.account_service.infrastructure.persistence.jpa;

import com.devsu.financial.account_service.domain.models.enums.AccountType;
import com.devsu.financial.account_service.infrastructure.entities.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AccountJpaRepository extends JpaRepository<AccountEntity, UUID> {

    boolean existsByCustomerIdAndAccountType(UUID customerId, AccountType accountType);
    Optional<AccountEntity> findByAccountNumber(String accountNumber);
}
