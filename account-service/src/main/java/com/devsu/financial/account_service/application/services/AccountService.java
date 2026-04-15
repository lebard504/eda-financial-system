package com.devsu.financial.account_service.application.services;

import com.devsu.financial.account_service.domain.models.Account;
import com.devsu.financial.account_service.domain.models.enums.AccountType;
import com.devsu.financial.account_service.domain.repositories.IAccountRepository;
import com.devsu.financial.account_service.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final IAccountRepository accountRepository;

    public Account create(Account account) {
        return accountRepository.save(account);
    }

    public Account getById(UUID id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Account not found"));
    }

    public List<Account> getAll() {
        return accountRepository.findAll();
    }

    public Account update(Account account) {
        if (account.getId() == null) {
            throw new IllegalArgumentException("Account ID is required for update");
        }

        getById(account.getId());
        return accountRepository.save(account);
    }

    public void delete(UUID id) {
        getById(id);
        accountRepository.deleteById(id);
    }

    public boolean existsByCustomerIdAndType(UUID customerId, AccountType type) {
        return accountRepository.existsByCustomerIdAndAccountType(customerId, type);
    }

    public Account getByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new NotFoundException("Account not found"));
    }
}