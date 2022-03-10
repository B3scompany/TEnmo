package com.techelevator.tenmo.exception;

public class UserNotFoundException extends Exception {
    public UserNotFoundException(String message){
        super(message);
    }

    public UserNotFoundException(){
        this("User not found.");
    }
}
