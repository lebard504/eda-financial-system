package com.devsu.financial.account_service.domain.models;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerSnapshot {

    private UUID customerId;
    private String name;
    private String identification;
}