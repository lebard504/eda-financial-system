package com.devsu.financial.account_service.interfaces.controllers;

import com.devsu.financial.account_service.application.services.AccountService;
import com.devsu.financial.account_service.domain.models.Account;
import com.devsu.financial.account_service.shared.utils.ResponseBuilder;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Account account) {
        Account created = accountService.create(account);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ResponseBuilder.success("Account created successfully", created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        Account account = accountService.getById(id);
        return ResponseEntity.ok(
                ResponseBuilder.success("Account found", account)
        );
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<Account> accounts = accountService.getAll();
        return ResponseEntity.ok(
                ResponseBuilder.success("Accounts retrieved", accounts)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody Account account) {
        account.setId(id);
        Account updated = accountService.update(account);
        return ResponseEntity.ok(
                ResponseBuilder.success("Account updated successfully", updated)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        accountService.delete(id);
        return ResponseEntity.ok(
                ResponseBuilder.success("Account deleted successfully", null)
        );
    }

    @GetMapping("/number/{accountNumber}")
    public ResponseEntity<?> getByAccountNumber(@PathVariable String accountNumber) {
        Account account = accountService.getByAccountNumber(accountNumber);
        return ResponseEntity.ok(
                ResponseBuilder.success("Account found", account)
        );
    }
}