package com.devsu.financial.account_service.infrastructure.persistence.adapters;

import com.devsu.financial.account_service.domain.models.Account;
import com.devsu.financial.account_service.domain.models.enums.AccountType;
import com.devsu.financial.account_service.domain.repositories.IAccountRepository;
import com.devsu.financial.account_service.infrastructure.entities.AccountEntity;
import com.devsu.financial.account_service.infrastructure.persistence.jpa.AccountJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class AccountRepositoryImpl implements IAccountRepository {

    private final AccountJpaRepository jpa;

    @Override
    public Account save(Account account) {
        AccountEntity entity = mapToEntity(account);
        AccountEntity saved = jpa.save(entity);
        return mapToDomain(saved);
    }

    @Override
    public Optional<Account> findById(UUID id) {
        return jpa.findById(id).map(this::mapToDomain);
    }

    @Override
    public List<Account> findAll() {
        return jpa.findAll()
                .stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        if (id != null) {
            jpa.deleteById(id);
        } 
        else {
            throw new IllegalArgumentException("ID must not be null");
        }
    }

    @Override
    public boolean existsByCustomerIdAndAccountType(UUID customerId, AccountType accountType) {
        return jpa.existsByCustomerIdAndAccountType(customerId, accountType);
    }

    @Override
    public Optional<Account> findByAccountNumber(String accountNumber) {
        return jpa.findByAccountNumber(accountNumber)
                .map(this::mapToDomain);
    }

    private AccountEntity mapToEntity(Account account) {
        return AccountEntity.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .accountType(account.getAccountType())
                .balance(account.getBalance() != null ? account.getBalance() : BigDecimal.ZERO)
                .status(account.getStatus())
                .customerId(account.getCustomerId())
                .build();
    }

    private Account mapToDomain(AccountEntity entity) {
        return Account.builder()
                .id(entity.getId())
                .accountNumber(entity.getAccountNumber())
                .accountType(entity.getAccountType())
                .balance(entity.getBalance())
                .status(entity.getStatus())
                .customerId(entity.getCustomerId())
                .build();
    }
}