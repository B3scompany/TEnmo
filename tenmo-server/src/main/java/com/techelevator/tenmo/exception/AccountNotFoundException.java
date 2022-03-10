package com.techelevator.tenmo.exception;

public class AccountNotFoundException extends Exception {
    public AccountNotFoundException(String message){
        super(message);
    }

    public AccountNotFoundException(){
        this("Transfer not found.");
    }
}
