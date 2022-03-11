package com.techelevator.tenmo.model;


public class Transfer {


    private int transferId;
    private String transferType;
    private String transferStatus;
    private UserPublicData fromUser;
    private UserPublicData toUser;
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

    public UserPublicData getFromUser() {
        return fromUser;
    }

    public Transfer setFromUser(UserPublicData fromUser) {
        this.fromUser = fromUser;
        return this;
    }

    public UserPublicData getToUser() {
        return toUser;
    }

    public Transfer setToUser(UserPublicData toUser) {
        this.toUser = toUser;
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