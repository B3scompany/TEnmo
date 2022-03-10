package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.exception.TransferNotFoundException;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;
import java.util.List;

public class TransferController {
    private final AccountDao accountDao;
    private final TransferDao transferDao;
    private final UserDao userDao;

    public TransferController(AccountDao accountDao, TransferDao transferDao, UserDao userDao) {
        this.accountDao = accountDao;
        this.transferDao = transferDao;
        this.userDao = userDao;
    }

    // listTransfers(Principal principal) Robert
    @RequestMapping(path = "/transfers", method = RequestMethod.GET)
    public List<Transfer> transferList(Principal principal){
        return transferDao.getAllTransfersForUser(userDao.findIdByUsername(principal.getName()));
    }


    // completeTransfer(int fromAccountID, int toAccountId, double amount) Scott


    // getTransferById(int transferId) Robert
    @RequestMapping(path = "/transfers/{id}", method = RequestMethod.GET)
    public Transfer getTransferById(@PathVariable("id") int transferID) throws TransferNotFoundException {
        return transferDao.getTransferById(transferID);


    }


    // private createTransfer(Transfer transfer) Scott



}
