package com.task.fibank.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;


public class TransactionRequestDto {
    @NotBlank(message = "Cashier name must not be blank")
    private String cashierName;

    @NotBlank(message = "Transaction type must not be blank (Deposit, Withdrawal, DepositEUR, WithdrawalEUR)")
    private String type; // Transaction type; can be "Deposit", "Withdrawal", "DepositEUR", "WithdrawalEUR"

    @Positive(message = "Amount must be positive")
    private double amount;

    public TransactionRequestDto() {}

    public String getCashierName() {
        return cashierName;
    }

    public void setCashierName(String cashierName) {
        this.cashierName = cashierName;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
}
