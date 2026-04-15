package com.devsu.financial.customer_service.interfaces.dto;

import lombok.Data;

@Data
public class UpdateCustomerRequest {
    private String name;
    private String address;
    private String phone;
    private Boolean status;
}