package com.techelevator.tenmo.model;

import com.techelevator.tenmo.dao.UserDao;

import javax.validation.constraints.Min;


public class Account {

    private int accountId;
    @Min(value = 1, message = "User Id should be a positive integer value")
    private int userId;
    private double balance;
    private UserDao userDao;

    public Account(UserDao userDao){
        this.userDao = userDao;
    }

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

    public void addToBalance(double amount){
        if(amount < 0){
            throw new IllegalStateException("Credit amount must be positive.");
        }
        balance = balance + amount;
    }

    public void subtractFromBalance(double amount){
        if(amount < 0 || amount > balance){
            throw new IllegalStateException("Debit amount must be a positive number less than the balance.");
        }
        balance = balance - amount;
    }

    public User getAccountUser(){
        return userDao.findByUserId(userId);
    }

}
