package com.task.fibank.dto;

import jakarta.validation.constraints.NotBlank;

public class BalanceRequestDto {
    @NotBlank(message = "Cashier name must not be blank")
    private String cashierName;
    private String dateFrom; // Optional
    private String dateTo;   // Optional

    public BalanceRequestDto() {}

    public String getCashierName() {
        return cashierName;
    }

    public void setCashierName(String cashierName) {
        this.cashierName = cashierName;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

}
