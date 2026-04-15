package com.devsu.financial.account_service.domain.repositories;

import com.devsu.financial.account_service.domain.models.Transaction;

import java.util.Optional;
import java.util.UUID;

public interface ITransactionRepository extends IBaseRepository<Transaction, UUID> {

    Optional<Transaction> findByIdempotencyKey(String idempotencyKey);
}
