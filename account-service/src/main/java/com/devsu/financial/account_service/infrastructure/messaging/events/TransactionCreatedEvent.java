package com.devsu.financial.account_service.infrastructure.messaging.events;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionCreatedEvent {
    private UUID transactionId;
    private UUID accountId;
    private BigDecimal amount;
    private BigDecimal balance;
}