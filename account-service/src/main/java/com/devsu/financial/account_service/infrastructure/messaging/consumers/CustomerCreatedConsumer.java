package com.devsu.financial.account_service.infrastructure.messaging.consumers;

import com.devsu.financial.account_service.application.services.AccountService;
import com.devsu.financial.account_service.config.AppConfig;
import com.devsu.financial.account_service.domain.models.Account;
import com.devsu.financial.account_service.domain.models.enums.AccountStatus;
import com.devsu.financial.account_service.domain.models.enums.AccountType;
import com.devsu.financial.account_service.infrastructure.messaging.events.CustomerCreatedEvent;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerCreatedConsumer {

    private final AccountService accountService;
    private final AppConfig appConfig;

    @KafkaListener(topics = "customer.created", groupId = "account-group")
    public void consume(CustomerCreatedEvent event) {
        if (event == null || event.getCustomerId() == null || event.getName() == null) {
            System.err.println("Invalid event received for account creation");
            return;
        }

        System.out.println("EVENT RECEIVED: " + event);

        // Feature toggle
        if (!appConfig.isCreateDefaultAccount()) {
            System.out.println("Feature disabled: skipping default account creation");
            return;
        }


        try {
            System.out.println("Checking if default account already exists for customer: " + event.getCustomerId());

            boolean exists = accountService.existsByCustomerIdAndType(
                event.getCustomerId(),
                AccountType.SAVINGS
            );

            if (exists) {
                System.out.println("Default account already exists, skipping...");
                return;
            }

            System.out.println("Creating default account for customer: " + event.getCustomerId());

            Account account = Account.builder()
                .accountNumber(generateAccountNumber())
                .accountType(AccountType.SAVINGS)
                .balance(BigDecimal.ZERO)
                .status(AccountStatus.ACTIVE)
                .customerId(event.getCustomerId())
                .build();

            accountService.create(account);

        } 
        catch (Exception ex) {
            System.err.println("Error processing customer.created event: " + ex.getMessage());
            ex.printStackTrace();
        }

        System.out.println("Finished processing event for customer: " + event.getCustomerId());
    }

    private String generateAccountNumber() {
        return "ACC-" + System.currentTimeMillis();
    }
}