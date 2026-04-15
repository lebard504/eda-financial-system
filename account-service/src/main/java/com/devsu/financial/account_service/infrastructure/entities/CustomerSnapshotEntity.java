package com.devsu.financial.account_service.infrastructure.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "customer_snapshots")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerSnapshotEntity {

    @Id
    private UUID customerId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = true)
    private String identification;
}