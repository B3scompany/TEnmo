package com.techelevator.tenmo.services;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.exception.AccountNotFoundException;
import com.techelevator.tenmo.exception.TransferNotFoundException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.security.Principal;

@Component
public class ServerTransferServiceImpl implements ServerTransferService {

    private TransferDao transferDao;
    private AccountDao accountDao;
    private UserDao userDao;

    @Autowired
    public ServerTransferServiceImpl(TransferDao transferDao, AccountDao accountDao, UserDao userDao) {
        this.transferDao = transferDao;
        this.accountDao = accountDao;
        this.userDao = userDao;
    }

    @Override
    public Transfer completeTransfer(Transfer transfer) throws AccountNotFoundException, TransferNotFoundException {

        validTransferCheck(transfer);

        transfer.setTransferStatus("Approved");

        Account fromAccount = accountDao.getAllAccountsByUser(transfer.getFromUser().getId()).get(0);
        Account toAccount = accountDao.getAllAccountsByUser(transfer.getToUser().getId()).get(0);
        double amount = transfer.getAmount();

        fromAccount.subtractFromBalance(amount);
        toAccount.addToBalance(amount);

        accountDao.update(fromAccount, fromAccount.getAccountId());
        accountDao.update(toAccount, toAccount.getAccountId());
        return transferDao.create(transfer);

    }

    @Override
    public Transfer saveTransferRequest(Transfer transfer) throws AccountNotFoundException, TransferNotFoundException {
        transfer.setTransferStatus("Pending");
        validAccountsForTransferCheck(transfer);
        return transferDao.create(transfer);
    }

    @Override
    public boolean isPrincipalPartyToTransfer(Principal principal, Transfer transfer) throws AccountNotFoundException {
        return isPrincipalFromAccountUser(principal, transfer) || isPrincipalToAccountUser(principal, transfer);
    }

    @Override
    public boolean isPrincipalFromAccountUser(Principal principal, Transfer transfer) throws AccountNotFoundException {
        int userId = userDao.findIdByUsername(principal.getName());;
        return userId == transfer.getFromUser().getId();
    }

    @Override
    public boolean isPrincipalToAccountUser(Principal principal, Transfer transfer) throws AccountNotFoundException {
        int userId = userDao.findIdByUsername(principal.getName());
        return userId == transfer.getToUser().getId();
    }

    private boolean hasSufficientFunds(Account fromAccount, double amount){
        return fromAccount.getBalance() >= amount;
    }

    private void validTransferCheck(Transfer transfer) throws AccountNotFoundException {
        validAccountsForTransferCheck(transfer);

        Account fromAccount = accountDao.getAllAccountsByUser(transfer.getFromUser().getId()).get(0);
        double amount = transfer.getAmount();

        if(!hasSufficientFunds(fromAccount, amount)){
            throw new IllegalStateException("Account from balance must be equal to or greater than transfer amount)");
        }
    }

    private void validAccountsForTransferCheck(Transfer transfer) throws AccountNotFoundException {
        Account fromAccount = accountDao.getAllAccountsByUser(transfer.getFromUser().getId()).get(0);
        Account toAccount = accountDao.getAllAccountsByUser(transfer.getToUser().getId()).get(0);

        if(fromAccount.getAccountId() == toAccount.getAccountId()){
            throw new IllegalArgumentException("Accounts cannot be the same.");
        }
    }
}
