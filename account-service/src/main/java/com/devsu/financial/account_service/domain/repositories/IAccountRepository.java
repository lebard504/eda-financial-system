package com.devsu.financial.account_service.domain.repositories;

import com.devsu.financial.account_service.domain.models.Account;
import com.devsu.financial.account_service.domain.models.enums.AccountType;

import java.util.Optional;
import java.util.UUID;

public interface IAccountRepository extends IBaseRepository<Account, UUID> {
    Optional<Account> findByAccountNumber(String accountNumber);
    boolean existsByCustomerIdAndAccountType(UUID customerId, AccountType accountType);
}
