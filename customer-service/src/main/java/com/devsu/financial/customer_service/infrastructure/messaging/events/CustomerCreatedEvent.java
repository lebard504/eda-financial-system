package com.devsu.financial.customer_service.infrastructure.messaging.events;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerCreatedEvent {

    private UUID customerId;

    private BigDecimal initialBalance;

    private String name;
    private String identification;
}