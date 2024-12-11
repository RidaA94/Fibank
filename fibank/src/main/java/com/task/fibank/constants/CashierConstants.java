package com.task.fibank.constants;



public class CashierConstants {
    // Status codes
    public static final int STATUS_201 = 201;
    public static final int STATUS_200 = 200;
    public static final int STATUS_400 = 400;
    public static final int STATUS_404 = 404;
    public static final int STATUS_500 = 500;

    // Messages
    public static final String MESSAGE_201 = "Deposit successful.";
    public static final String MESSAGE_200 = "Withdraw successful.";
    public static final String MESSAGE_400 = "Invalid transaction type. Use 'Deposit' or 'Withdrawal'.";
    public static final String MESSAGE_404 = "Cashier not found.";
    public static final String MESSAGE_500 = "Transaction processing error.";

    @Override
    public String toString() {
        return "CashierConstants{}";
    }
}
