package com.devsu.financial.account_service.application.queries.dto;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.devsu.financial.account_service.domain.models.enums.AccountType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountReport {
    private UUID accountId;
    private String accountNumber;
    private AccountType accountType;
    private BigDecimal balance;
    private List<TransactionReport> transactions;
}