package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.TransferNotFoundException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {

    public Transfer create(Transfer transfer) throws TransferNotFoundException;
    public Transfer getTransferById(int transferId) throws TransferNotFoundException;
    public List<Transfer> getAllTransfersForUser(int userId);
    public Transfer updateTransfer(Transfer transfer, int transferId) throws TransferNotFoundException;
    public boolean deleteTransferById(int transferId) throws TransferNotFoundException;

}
