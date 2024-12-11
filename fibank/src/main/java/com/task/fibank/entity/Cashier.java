package com.task.fibank.entity;

import jakarta.persistence.*;
@Entity
@Table(name = "cashiers")
public class Cashier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private double balanceBGN;

    @Column(nullable = false)
    private double balanceEUR;

    // Added denominations as a string property for simplicity
    @Column(nullable = false)
    private String denominationsBGN;

    @Column(nullable = false)
    private String denominationsEUR;

    public Cashier() {}

    public Cashier(String name, double balanceBGN, double balanceEUR, String denominationsBGN, String denominationsEUR) {
        this.name = name;
        this.balanceBGN = balanceBGN;
        this.balanceEUR = balanceEUR;
        this.denominationsBGN = denominationsBGN;
        this.denominationsEUR = denominationsEUR;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBalanceBGN() {
        return balanceBGN;
    }

    public void setBalanceBGN(double balanceBGN) {
        this.balanceBGN = balanceBGN;
    }

    public double getBalanceEUR() {
        return balanceEUR;
    }

    public void setBalanceEUR(double balanceEUR) {
        this.balanceEUR = balanceEUR;
    }

    public String getDenominationsBGN() {
        return denominationsBGN;
    }

    public String getDenominationsEUR() {
        return denominationsEUR;
    }
}
