package com.devsu.financial.account_service.interfaces.controllers;

import com.devsu.financial.account_service.application.services.TransactionService;
import com.devsu.financial.account_service.domain.models.Transaction;
import com.devsu.financial.account_service.shared.utils.ResponseBuilder;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Transaction transaction) {
        Transaction created = transactionService.create(transaction);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ResponseBuilder.success("Transaction created successfully", created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        Transaction transaction = transactionService.getById(id);
        return ResponseEntity.ok(
                ResponseBuilder.success("Transaction found", transaction)
        );
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<Transaction> transactions = transactionService.getAll();
        return ResponseEntity.ok(
                ResponseBuilder.success("Transactions retrieved", transactions)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        transactionService.delete(id);
        return ResponseEntity.ok(
                ResponseBuilder.success("Transaction deleted successfully", null)
        );
    }
}