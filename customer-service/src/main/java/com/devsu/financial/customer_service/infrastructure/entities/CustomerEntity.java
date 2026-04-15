package com.devsu.financial.customer_service.infrastructure.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerEntity {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;
    private String gender;
    private Integer age;

    @Column(nullable = false, unique = true)
    private String identification;

    private String address;
    private String phone;

    private String password;

    private Boolean status;
}