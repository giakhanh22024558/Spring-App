package com.example.demo.controller;

import com.example.demo.model.transactions.Transaction;
import com.example.demo.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "keycloak")
@RestController
@RequestMapping("/store/api")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/transactions")
    public ResponseEntity<?> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    @GetMapping("/transactions/{id}")
    public ResponseEntity<?> getTransactionById(@PathVariable Integer id) {
        return transactionService.getTransactionById(id);
    }

    @PostMapping("/transactions")
    public ResponseEntity<?> createTransaction(@RequestBody Transaction transaction) {
        return transactionService.saveTransaction(transaction);
    }

    @PutMapping("/transactions/{id}")
    public ResponseEntity<?> updateTransaction(@PathVariable Integer id, @RequestBody Transaction transaction) {
        transaction.setId(id);
        return transactionService.saveTransaction(transaction);
    }

    @DeleteMapping("/transactions/{id}")
    public ResponseEntity<?> deleteTransaction(@PathVariable Integer id) {
        return transactionService.deleteTransaction(id);
    }
}