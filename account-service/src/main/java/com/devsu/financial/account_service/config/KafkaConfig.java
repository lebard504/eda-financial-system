package com.devsu.financial.account_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic customerTopic() {
        return new NewTopic("customer.created", 1, (short) 1);
    }

    @Bean
    public NewTopic transactionTopic() {
        return new NewTopic("transaction.created", 1, (short) 1);
    }
}