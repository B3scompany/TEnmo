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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;

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


    // private createTransfer(Transfer transfer) Scott



}
