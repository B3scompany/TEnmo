package com.techelevator.tenmo.exception;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class TransferNotFoundException extends Exception {

    public TransferNotFoundException(String message){
        super(message);
    }

    public TransferNotFoundException(){
        this("Transfer not found.");
    }
}
