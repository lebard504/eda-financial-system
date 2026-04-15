package com.devsu.financial.customer_service.domain.models;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Customer extends Person {
    private UUID id;
    private String password;
    private Boolean status;
}