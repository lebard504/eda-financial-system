package com.devsu.financial.customer_service.infrastructure.persistence.adapters;

import com.devsu.financial.customer_service.domain.models.Customer;
import com.devsu.financial.customer_service.domain.repositories.ICustomerRepository;
import com.devsu.financial.customer_service.infrastructure.entities.CustomerEntity;
import com.devsu.financial.customer_service.infrastructure.persistence.jpa.CustomerJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CustomerRepositoryImpl implements ICustomerRepository {

    private final CustomerJpaRepository jpa;

    @Override
    public Customer save(Customer customer) {
        CustomerEntity entity = mapToEntity(customer);
        CustomerEntity saved = jpa.save(entity);
        return mapToDomain(saved);
    }

    @Override
    public Optional<Customer> findById(UUID id) {
        return jpa.findById(id).map(this::mapToDomain);
    }

    @Override
    public List<Customer> findAll() {
        return jpa.findAll()
                .stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        jpa.deleteById(id);
    }

    @Override
    public Optional<Customer> findByIdentification(String identification) {
        return jpa.findByIdentification(identification)
                .map(this::mapToDomain);
    }

    // =========================
    // MAPPERS
    // =========================

    private CustomerEntity mapToEntity(Customer c) {
        return CustomerEntity.builder()
                .id(c.getId())
                .name(c.getName())
                .gender(c.getGender())
                .age(c.getAge())
                .identification(c.getIdentification())
                .address(c.getAddress())
                .phone(c.getPhone())
                .password(c.getPassword())
                .status(c.getStatus())
                .build();
    }

    private Customer mapToDomain(CustomerEntity e) {

        Customer c = Customer.builder()
                .id(e.getId())
                .password(e.getPassword())
                .status(e.getStatus())
                .build();
    
        c.setName(e.getName());
        c.setGender(e.getGender());
        c.setAge(e.getAge());
        c.setIdentification(e.getIdentification());
        c.setAddress(e.getAddress());
        c.setPhone(e.getPhone());
    
        return c;
    }
}