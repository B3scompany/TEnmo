package com.techelevator.tenmo.model;

import javax.validation.constraints.*;

public class Transfer {

    private int transferId;

    @NotBlank
    private String transferType;
    @NotBlank
    private String transferStatus;

    @NotNull
    private UserPublicData fromUser;

    @NotNull
    private UserPublicData toUser;

    @Positive(message = "transfer amount must be positive.")
    private double amount;

    public Transfer(){}

    public Transfer(int transferId, String transferType, String transferStatus, UserPublicData fromUser, UserPublicData toUser, double amount) {
        this.transferId = transferId;
        this.transferType = transferType;
        this.transferStatus = transferStatus;
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.amount = amount;
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
