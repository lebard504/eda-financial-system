package com.devsu.financial.account_service.domain.models;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.devsu.financial.account_service.domain.models.enums.TransactionType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {
    private UUID id;
    private LocalDateTime transactionDate;
    private TransactionType transactionType;
    private BigDecimal amount;
    private BigDecimal balance;
    private String idempotencyKey;
    private UUID accountId;
}
