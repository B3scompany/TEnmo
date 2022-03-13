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
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
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
    public List<Transfer> transferList(Principal principal) throws AuthorizationException {
        if(principal == null) {
            throw new AuthorizationException();
        }
        return transferDao.getAllTransfersForUser(userDao.findIdByUsername(principal.getName()));
    }


    // completeTransfer(Transfer) Scott
    @Transactional
    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(path = "/transfers", method = RequestMethod.POST)
    public Transfer completeTransfer(@RequestBody @Valid Transfer transfer, Principal principal) throws TransferNotFoundException, AccountNotFoundException, AuthorizationException {

        if(transfer.getTransferType().equalsIgnoreCase("Send")) {
            if (!transferService.isPrincipalFromAccountUser(principal, transfer)) {
                throw new AuthorizationException();
            }
            return transferService.completeTransfer(transfer);
        } else {
            if(!transferService.isPrincipalToAccountUser(principal, transfer)){
                throw new AuthorizationException();
            }
            transfer.setTransferStatus("Pending");
            return transferService.saveTransferRequest(transfer);
        }
    }

    @RequestMapping(path = "transfer/{id}/approve}", method = RequestMethod.PUT)
    public Transfer approvePendingTransfer(@PathVariable(value = "id") int transferId, Principal principal) throws TransferNotFoundException, AccountNotFoundException {
        Transfer transfer = transferDao.getTransferById(transferId);

        if(!transferService.isPrincipalFromAccountUser(principal, transfer)){
            throw new IllegalStateException("Only the user from whose account the transfer is requested may approve the transfer.");
        }

        return transferService.approvePendingTransfer(transfer);

    }

    @RequestMapping(path = "transfer/{id}/reject}", method = RequestMethod.PUT)
    public Transfer rejectPendingTransfer(@PathVariable(value = "id") int transferId, Principal principal) throws TransferNotFoundException, AccountNotFoundException {
        Transfer transfer = transferDao.getTransferById(transferId);

        if(!transferService.isPrincipalFromAccountUser(principal, transfer)){
            throw new IllegalStateException("Only the user from whose account the transfer is requested may reject the transfer.");
        }

        return transferService.rejectPendingTransfer(transfer);

    }

    // getTransferById(int transferId) Robert
    @RequestMapping(path = "/transfers/{id}", method = RequestMethod.GET)
    public Transfer getTransferById(@PathVariable("id") int transferID) throws TransferNotFoundException {
        return transferDao.getTransferById(transferID);


    }


    // private createTransfer(Transfer transfer) Scott



}
