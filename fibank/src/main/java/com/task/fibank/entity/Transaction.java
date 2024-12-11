package com.task.fibank.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cashier_id", referencedColumnName = "id")
    private Cashier cashier;

    @Column(nullable = false)
    private String type; // Deposit/Withdrawal

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    public Cashier getCashier() {
        return cashier;
    }

    public void setCashier(Cashier cashier) {
        this.cashier = cashier;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }
}
