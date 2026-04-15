package com.devsu.financial.customer_service.interfaces.controllers;

import com.devsu.financial.customer_service.application.services.CustomerService;
import com.devsu.financial.customer_service.domain.models.Customer;
import com.devsu.financial.customer_service.interfaces.dto.UpdateCustomerRequest;
import com.devsu.financial.customer_service.shared.utils.ResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService service;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Customer customer) {
        Customer created = service.create(customer);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ResponseBuilder.success(201, "Customer created successfully", created));
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<Customer> customers = service.getAll();
        return ResponseEntity.ok(
                ResponseBuilder.success("Customers retrieved successfully", customers)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        Customer customer = service.getById(id);
        return ResponseEntity.ok(
                ResponseBuilder.success("Customer retrieved successfully", customer)
        );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> patch(
            @PathVariable UUID id,
            @RequestBody UpdateCustomerRequest req
    ) {
        Customer updated = service.patch(id, req);

        return ResponseEntity.ok(
                ResponseBuilder.success("Customer partially updated", updated)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody Customer customer) {
        customer.setId(id);
        Customer updated = service.update(customer);
        return ResponseEntity.ok(
                ResponseBuilder.success("Customer updated successfully", updated)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.ok(
                ResponseBuilder.success("Customer deleted successfully", null)
        );
    }
}