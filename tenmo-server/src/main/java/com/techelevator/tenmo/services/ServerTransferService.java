package com.techelevator.tenmo.services;

import com.techelevator.tenmo.exception.AccountNotFoundException;
import com.techelevator.tenmo.exception.TransferNotFoundException;
import com.techelevator.tenmo.model.Transfer;

import java.security.Principal;


public interface ServerTransferService {

    public Transfer completeTransfer(Transfer transfer) throws AccountNotFoundException, TransferNotFoundException;
    public Transfer saveTransferRequest(Transfer transfer) throws AccountNotFoundException, TransferNotFoundException;
    public boolean isPrincipalPartyToTransfer(Principal principal, Transfer transfer) throws AccountNotFoundException;
    public boolean isPrincipalFromAccountUser(Principal principal, Transfer transfer) throws AccountNotFoundException;
    public boolean isPrincipalToAccountUser(Principal principal, Transfer transfer) throws AccountNotFoundException;
}
