package com.devsu.financial.account_service.domain.models;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

import com.devsu.financial.account_service.domain.models.enums.AccountStatus;
import com.devsu.financial.account_service.domain.models.enums.AccountType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {
    private UUID id;
    private String accountNumber;
    private AccountType accountType;
    @Builder.Default
    private BigDecimal balance = BigDecimal.ZERO;
    private AccountStatus status;
    private UUID customerId;
}
