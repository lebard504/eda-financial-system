package com.devsu.financial.customer_service.application.services;

import com.devsu.financial.customer_service.domain.models.Customer;
import com.devsu.financial.customer_service.domain.repositories.ICustomerRepository;
import com.devsu.financial.customer_service.exceptions.NotFoundException;
import com.devsu.financial.customer_service.infrastructure.messaging.events.CustomerCreatedEvent;
import com.devsu.financial.customer_service.infrastructure.messaging.producers.CustomerEventProducer;
import com.devsu.financial.customer_service.interfaces.dto.UpdateCustomerRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final ICustomerRepository repository;
    private final CustomerEventProducer producer;

    public Customer create(Customer customer) {

        Customer saved = repository.save(customer);

        producer.publish(
            CustomerCreatedEvent.builder()
                    .customerId(saved.getId())
                    .name(saved.getName())
                    .build()
        );

        return saved;
    }

    public Customer getById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer not found"));
    }

    public List<Customer> getAll() {
        return repository.findAll();
    }

    public Customer patch(UUID id, UpdateCustomerRequest req) {
        Customer existing = repository.findById(id)
            .orElseThrow(() -> new NotFoundException("Customer not found"));

        if (req.getName() != null) {
            existing.setName(req.getName());
        }

        if (req.getAddress() != null) {
            existing.setAddress(req.getAddress());
        }

        if (req.getPhone() != null) {
            existing.setPhone(req.getPhone());
        }

        if (req.getStatus() != null) {
            existing.setStatus(req.getStatus());
        }

        return repository.save(existing);
    }

    public Customer update(Customer customer) {

        if (customer.getId() == null) {
            throw new IllegalArgumentException("Customer ID is required");
        }

        getById(customer.getId());

        return repository.save(customer);
    }

    public void delete(UUID id) {
        getById(id);
        repository.deleteById(id);
    }
}