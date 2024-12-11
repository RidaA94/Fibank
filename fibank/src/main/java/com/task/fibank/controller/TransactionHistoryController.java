package com.task.fibank.controller;

import com.task.fibank.entity.Transaction;
import com.task.fibank.exception.CashierNotFoundException;
import com.task.fibank.service.ICashOperationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transaction-history")
public class TransactionHistoryController {
    private final ICashOperationsService cashOperationsService;

    @Autowired
    public TransactionHistoryController(ICashOperationsService cashOperationsService) {
        this.cashOperationsService = cashOperationsService;
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getTransactionHistory(@RequestParam String cashierName) {
        return cashOperationsService.getTransactionHistory(cashierName)
                .map(transactions -> ResponseEntity.ok(transactions))
                .orElseThrow(() -> new CashierNotFoundException(cashierName));
    }
}
