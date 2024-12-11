package com.task.fibank.dto;


import java.util.List;


public class BalanceResponseDto {
    private String cashierName;
    private double balanceBGN;
    private double balanceEUR;
    private List<String> denominationsBGN; // List of BGN denominations
    private List<String> denominationsEUR; // List of EUR denominations

    public BalanceResponseDto(String cashierName, double balanceBGN, double balanceEUR, List<String> denominationsBGN, List<String> denominationsEUR) {
        this.cashierName = cashierName;
        this.balanceBGN = balanceBGN;
        this.balanceEUR = balanceEUR;
        this.denominationsBGN = denominationsBGN;
        this.denominationsEUR = denominationsEUR;
    }

    public String getCashierName() {
        return cashierName;
    }
    public double getBalanceBGN() {
        return balanceBGN;
    }
    public double getBalanceEUR() {
        return balanceEUR;
    }

    public List<String> getDenominationsBGN() {
        return denominationsBGN;
    }

    public List<String> getDenominationsEUR() {
        return denominationsEUR;
    }
}
