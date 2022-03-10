package com.techelevator.tenmo.model;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.exception.AccountNotFoundException;

import javax.validation.constraints.*;
import java.security.Principal;

public class Transfer {

    private int transferId;

    @NotBlank
    private String transferType;
    @NotBlank
    private String transferStatus;

    @NotNull
    @Positive(message = "accountFromId should be a positive integer value.")
    private int accountFromId;

    @NotNull
    @Positive(message = "accountToId should be a positive integer value.")
    private int accountToId;

    @Positive(message = "transfer amount must be positive.")
    private double amount;

    private AccountDao accountDao;

    public Transfer(AccountDao accountDao){
        this.accountDao = accountDao;
    }

    public int getTransferId() {
        return transferId;
    }

    public Transfer setTransferId(int transferId) {
        this.transferId = transferId;
        return this;
    }

    public String getTransferType() {
        return transferType;
    }

    public Transfer setTransferType(String transferType) {
        this.transferType = transferType;
        return this;
    }

    public String getTransferStatus() {
        return transferStatus;
    }

    public Transfer setTransferStatus(String transferStatus) {
        this.transferStatus = transferStatus;
        return this;
    }

    public int getAccountFromId() {
        return accountFromId;
    }

    public Transfer setAccountFromId(int accountFromId) {
        this.accountFromId = accountFromId;
        return this;
    }

    public int getAccountToId() {
        return accountToId;
    }

    public Transfer setAccountToId(int accountToId) {
        this.accountToId = accountToId;
        return this;
    }

    public double getAmount() {
        return amount;
    }

    public Transfer setAmount(double amount) {
        this.amount = amount;
        return this;
    }

    public Account getFromAccount() throws AccountNotFoundException {
        return accountDao.getAccountById(accountFromId);
    }
    public Account getToAccount() throws AccountNotFoundException {
        return accountDao.getAccountById(accountToId);
    }

}
