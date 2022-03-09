package com.techelevator.tenmo.model;

public class Transfer {

    private int transferId;
    private String transferType;
    private String transferStatus;
    private int accountFromId;
    private int accountToId;
    private double amount;

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
}
