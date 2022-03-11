package com.techelevator.tenmo.model;


public class Transfer {


    private int transferId;
    private String transferType;
    private String transferStatus;
    private int userFromId;
    private int userToId;
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

    public int getUserFromId() {
        return userFromId;
    }

    public Transfer setUserFromId(int userFromId) {
        this.userFromId = userFromId;
        return this;
    }

    public int getUserToId() {
        return userToId;
    }

    public Transfer setUserToId(int userToId) {
        this.userToId = userToId;
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