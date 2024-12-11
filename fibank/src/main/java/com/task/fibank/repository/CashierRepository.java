package com.task.fibank.repository;

import com.task.fibank.entity.Cashier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CashierRepository extends JpaRepository<Cashier, Long> {
    Cashier findByName(String name); // Method to find a cashier by their name
}
