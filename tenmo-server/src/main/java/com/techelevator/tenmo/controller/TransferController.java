package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.exception.AccountNotFoundException;
import com.techelevator.tenmo.exception.AuthorizationException;
import com.techelevator.tenmo.exception.TransferNotFoundException;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.services.ServerTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController

public class TransferController {
    private final AccountDao accountDao;
    private final TransferDao transferDao;
    private final UserDao userDao;
    private final ServerTransferService transferService;

    @Autowired
    public TransferController(AccountDao accountDao, TransferDao transferDao, UserDao userDao, ServerTransferService transferService) {
        this.accountDao = accountDao;
        this.transferDao = transferDao;
        this.userDao = userDao;
        this.transferService = transferService;
    }

    // listTransfers(Principal principal) Robert
    @RequestMapping(path = "/transfers", method = RequestMethod.GET)
    public List<Transfer> transferList(Principal principal){
        return transferDao.getAllTransfersForUser(userDao.findIdByUsername(principal.getName()));
    }


    // completeTransfer(Transfer) Scott
    @RequestMapping(path = "/transfers", method = RequestMethod.POST)
    public void completeTransfer(@RequestBody @Valid Transfer transfer, Principal principal) throws TransferNotFoundException, AccountNotFoundException, AuthorizationException {
        int fromAccountUserId = transfer.getFromAccount().getUserId();
        int principalUserId = userDao.findIdByUsername(principal.getName());
        if(fromAccountUserId != principalUserId){
            throw new AuthorizationException();
        }
        transferService.completeTransfer(transfer);
    }

    // getTransferById(int transferId) Robert
    @RequestMapping(path = "/transfers/{id}", method = RequestMethod.GET)
    public Transfer getTransferById(@PathVariable("id") int transferID) throws TransferNotFoundException {
        return transferDao.getTransferById(transferID);


    }


    // private createTransfer(Transfer transfer) Scott



}
