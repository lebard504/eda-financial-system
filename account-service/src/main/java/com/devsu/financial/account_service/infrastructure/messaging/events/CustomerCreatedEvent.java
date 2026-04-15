package com.devsu.financial.account_service.infrastructure.messaging.events;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerCreatedEvent {

    private UUID customerId;

    private BigDecimal initialBalance;

    private String name;
    private String identification;
}