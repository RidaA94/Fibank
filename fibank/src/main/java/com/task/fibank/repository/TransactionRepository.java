package com.task.fibank.repository;

import com.task.fibank.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByCashierIdAndTimestampBetween(int cashierId, LocalDateTime startDate, LocalDateTime endDate);
    List<Transaction> findByCashierId(int cashierId); // Find all transactions for a specific cashier
}
