package com.task.fibank.exception;

public class CashierNotFoundException extends RuntimeException {
    private String cashierName;

    public CashierNotFoundException(String cashierName) {
        super("Cashier with name " + cashierName + " not found.");
        this.cashierName = cashierName;
    }

    public String getCashierName() {
        return cashierName;
    }
}
