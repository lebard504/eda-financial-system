package com.devsu.financial.customer_service.application.services;

import com.devsu.financial.customer_service.domain.models.Customer;
import com.devsu.financial.customer_service.domain.repositories.ICustomerRepository;
import com.devsu.financial.customer_service.infrastructure.messaging.producers.CustomerEventProducer;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    private final ICustomerRepository repository = mock(ICustomerRepository.class);
    private final CustomerEventProducer producer = mock(CustomerEventProducer.class);

    private final CustomerService service = new CustomerService(repository, producer);

    @Test
    void shouldCreateCustomerSuccessfully() {

        Customer input = Customer.builder()
                .name("Jose Test")
                .build();

        Customer saved = Customer.builder()
                .id(UUID.randomUUID())
                .name("Jose Test")
                .build();

        when(repository.save(any())).thenReturn(saved);

        Customer result = service.create(input);

        assertNotNull(result.getId());
        assertEquals("Jose Test", result.getName());

        verify(repository, times(1)).save(any());
        verify(producer, times(1)).publish(any());
    }

    @Test
    void shouldReturnCustomerById() {

        UUID id = UUID.randomUUID();

        Customer customer = Customer.builder()
                .id(id)
                .name("Jose Test")
                .build();

        when(repository.findById(id)).thenReturn(Optional.of(customer));

        Customer result = service.getById(id);

        assertEquals(id, result.getId());
    }
}