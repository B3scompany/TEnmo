package com.techelevator.tenmo.model;

public class Account {

    private int accountId;
    private int userId;
    private double balance;

    public int getAccountId() {
        return accountId;
    }

    public Account setAccountId(int accountId) {
        this.accountId = accountId;
        return this;
    }

    public int getUserId() {
        return userId;
    }

    public Account setUserId(int userId) {
        this.userId = userId;
        return this;
    }

    public double getBalance() {
        return balance;
    }

    public Account setBalance(double balance) {
        this.balance = balance;
        return this;
    }
}
