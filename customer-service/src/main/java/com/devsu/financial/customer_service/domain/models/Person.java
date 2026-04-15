package com.devsu.financial.customer_service.domain.models;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Person {
    private String name;
    private String gender;
    private Integer age;
    private String identification;
    private String address;
    private String phone;
}