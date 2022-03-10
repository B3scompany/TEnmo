package com.techelevator.tenmo.services;

import com.techelevator.tenmo.exception.AccountNotFoundException;

public interface ServerTransferService {

    public void completeTransfer(int accountFromId, int accountToId, double amount) throws AccountNotFoundException;

}
