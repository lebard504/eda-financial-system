package com.devsu.financial.account_service.application.services;

import com.devsu.financial.account_service.domain.models.Account;
import com.devsu.financial.account_service.domain.models.Transaction;
import com.devsu.financial.account_service.domain.models.enums.TransactionType;
import com.devsu.financial.account_service.domain.repositories.IAccountRepository;
import com.devsu.financial.account_service.domain.repositories.ITransactionRepository;
import com.devsu.financial.account_service.exceptions.ApplicationException;
import com.devsu.financial.account_service.exceptions.BadRequestException;
import com.devsu.financial.account_service.exceptions.InsufficientFundsException;
import com.devsu.financial.account_service.exceptions.NotFoundException;
import com.devsu.financial.account_service.infrastructure.messaging.events.TransactionCreatedEvent;
import com.devsu.financial.account_service.infrastructure.messaging.producers.TransactionEventProducer;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final ITransactionRepository transactionRepository;
    private final IAccountRepository accountRepository;
    private final TransactionEventProducer producer;

    @Retry(name = "accountService", fallbackMethod = "fallbackCreate")
    @CircuitBreaker(name = "accountService", fallbackMethod = "fallbackCreate")
    @Transactional
    public Transaction create(Transaction transaction) {

        // Idempotency check
        transactionRepository.findByIdempotencyKey(transaction.getIdempotencyKey())
            .ifPresent(t -> {
                throw new BadRequestException("Duplicate transaction (idempotency key already used)");
            });

        // Validate account exists
        Account account = accountRepository.findById(transaction.getAccountId())
                .orElseThrow(() -> new NotFoundException("Account not found"));

        BigDecimal amount = transaction.getAmount();
        if (transaction.getTransactionType() == TransactionType.WITHDRAW) {
            amount = amount.negate();
        }


        BigDecimal newBalance = account.getBalance().add(amount);

        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientFundsException();
        }

        // Update account balance
        account.setBalance(newBalance);
        accountRepository.save(account);

        // Build transaction
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setBalance(newBalance);

        Transaction saved = transactionRepository.save(transaction);

        // Emit transaction event to enable async processing (fraud detection, monitoring, etc.)
        producer.publish(
            TransactionCreatedEvent.builder()
                .transactionId(saved.getId())
                .accountId(saved.getAccountId())
                .amount(saved.getAmount())
                .balance(saved.getBalance())
                .build()
        );

        return saved;
    }

    // CRUD operations
    public Transaction getById(UUID id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Transaction not found"));
    }

    public List<Transaction> getAll() {
        return transactionRepository.findAll();
    }

    public void delete(UUID id) {
        transactionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Transaction not found"));

        transactionRepository.deleteById(id);
    }

    public Transaction fallbackCreate(Transaction transaction, Throwable ex) {
        if (ex instanceof InsufficientFundsException
            || ex instanceof BadRequestException
            || ex instanceof NotFoundException) {
            throw (RuntimeException) ex;
        }
    
        System.err.println("Fallback triggered for transaction: " + transaction.getAccountId());
        System.err.println("Error: " + ex.getMessage());
    
        throw new ApplicationException("Service temporarily unavailable");
    }
}