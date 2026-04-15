package com.devsu.financial.account_service.integration;

import com.devsu.financial.account_service.application.services.TransactionService;
import com.devsu.financial.account_service.domain.models.Account;
import com.devsu.financial.account_service.domain.models.Transaction;
import com.devsu.financial.account_service.domain.models.enums.AccountStatus;
import com.devsu.financial.account_service.domain.models.enums.AccountType;
import com.devsu.financial.account_service.domain.models.enums.TransactionType;
import com.devsu.financial.account_service.domain.repositories.IAccountRepository;
import com.devsu.financial.account_service.domain.repositories.ITransactionRepository;
import com.devsu.financial.account_service.exceptions.InsufficientFundsException;
import com.devsu.financial.account_service.infrastructure.messaging.producers.TransactionEventProducer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class TransactionIntegrationTest {

    @Mock
    private ITransactionRepository transactionRepository;

    @Mock
    private IAccountRepository accountRepository;

    @Mock
    private TransactionEventProducer producer;

    @InjectMocks
    private TransactionService service;

    @Test
    void shouldCreateTransactionAndUpdateBalance() {
        UUID accountId = UUID.randomUUID();

        Account account = Account.builder()
                .id(accountId)
                .balance(BigDecimal.valueOf(1000))
                .status(AccountStatus.ACTIVE)
                .accountType(AccountType.SAVINGS)
                .build();

        when(accountRepository.findById(accountId))
                .thenReturn(Optional.of(account));

        when(transactionRepository.findByIdempotencyKey(any()))
                .thenReturn(Optional.empty());

        when(transactionRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Transaction tx = Transaction.builder()
                .accountId(accountId)
                .transactionType(TransactionType.WITHDRAW)
                .amount(BigDecimal.valueOf(200))
                .idempotencyKey("test-key-1")
                .build();

        Transaction result = service.create(tx);

        assertEquals(BigDecimal.valueOf(800), result.getBalance());

        verify(transactionRepository).save(any());
        verify(accountRepository).save(any());
        verify(producer).publish(any());
    }

    @Test
    void shouldThrowExceptionWhenInsufficientFunds() {
        UUID accountId = UUID.randomUUID();

        Account account = Account.builder()
                .id(accountId)
                .balance(BigDecimal.valueOf(100))
                .accountType(AccountType.SAVINGS)
                .status(AccountStatus.ACTIVE)
                .build();

        Transaction tx = new Transaction();
        tx.setAccountId(accountId);
        tx.setTransactionType(TransactionType.WITHDRAW);
        tx.setAmount(BigDecimal.valueOf(200));
        tx.setIdempotencyKey("test-insufficient");

        when(accountRepository.findById(accountId))
                .thenReturn(Optional.of(account));

        when(transactionRepository.findByIdempotencyKey(any()))
                .thenReturn(Optional.empty());

        assertThrows(InsufficientFundsException.class, () -> {
            service.create(tx);
        });

        verify(transactionRepository, never()).save(any());
        verify(accountRepository, never()).save(any());
        verify(producer, never()).publish(any());
    }
}