package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {

    public Transfer create(Transfer transfer);
    public Transfer getTransferById(int transferId);
    public List<Transfer> getAllTransfersForUser(int userId);
    public Transfer updateTransfer(Transfer transfer, int transferId);
    public boolean deleteTransferById(int transferId);

}
