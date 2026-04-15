package com.devsu.financial.account_service.infrastructure.persistence.adapters;

import com.devsu.financial.account_service.domain.models.Transaction;
import com.devsu.financial.account_service.domain.repositories.ITransactionRepository;
import com.devsu.financial.account_service.infrastructure.entities.AccountEntity;
import com.devsu.financial.account_service.infrastructure.entities.TransactionEntity;
import com.devsu.financial.account_service.infrastructure.persistence.jpa.AccountJpaRepository;
import com.devsu.financial.account_service.infrastructure.persistence.jpa.TransactionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class TransactionRepositoryImpl implements ITransactionRepository {

    private final TransactionJpaRepository transactionJpa;
    private final AccountJpaRepository accountJpa;

    @Override
    public Transaction save(Transaction transaction) {

        UUID accountId = transaction.getAccountId();
        if (accountId == null) {
            throw new IllegalArgumentException("Account ID cannot be null");
        }
        AccountEntity account = accountJpa.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        TransactionEntity entity = mapToEntity(transaction, account);
        TransactionEntity saved = transactionJpa.save(entity);

        return mapToDomain(saved);
    }

    @Override
    public Optional<Transaction> findById(UUID id) {
        return transactionJpa.findById(id)
                .map(this::mapToDomain);
    }

    @Override
    public List<Transaction> findAll() {
        return transactionJpa.findAll()
                .stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        transactionJpa.deleteById(id);
    }

    @Override
    public Optional<Transaction> findByIdempotencyKey(String idempotencyKey) {
        return transactionJpa.findByIdempotencyKey(idempotencyKey)
                .map(this::mapToDomain);
    }

    private TransactionEntity mapToEntity(Transaction transaction, AccountEntity account) {
        return TransactionEntity.builder()
                .id(transaction.getId())
                .transactionDate(transaction.getTransactionDate())
                .transactionType(transaction.getTransactionType())
                .amount(transaction.getAmount())
                .balance(transaction.getBalance())
                .idempotencyKey(transaction.getIdempotencyKey())
                .account(account)
                .build();
    }

    private Transaction mapToDomain(TransactionEntity entity) {
        return Transaction.builder()
                .id(entity.getId())
                .transactionDate(entity.getTransactionDate())
                .transactionType(entity.getTransactionType())
                .amount(entity.getAmount())
                .balance(entity.getBalance())
                .idempotencyKey(entity.getIdempotencyKey())
                .accountId(entity.getAccount().getId())
                .build();
    }
}