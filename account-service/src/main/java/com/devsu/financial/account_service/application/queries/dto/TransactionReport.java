package com.devsu.financial.account_service.application.queries.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionReport {
    private UUID transactionId;
    private LocalDateTime date;
    private String type;
    private BigDecimal amount;
    private BigDecimal balance;
}