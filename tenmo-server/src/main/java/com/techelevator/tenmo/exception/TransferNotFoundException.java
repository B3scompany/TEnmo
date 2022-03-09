package com.techelevator.tenmo.exception;

import com.techelevator.tenmo.model.Transfer;

public class TransferNotFoundException extends Exception {

    public TransferNotFoundException(String message){
        super(message);
    }

    public TransferNotFoundException(){
        this("Transfer not found.");
    }
}
