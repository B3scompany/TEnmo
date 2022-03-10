package com.techelevator.tenmo.services;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.exception.AccountNotFoundException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServerTransferServiceImpl implements ServerTransferService {

    private TransferDao transferDao;
    private AccountDao accountDao;

    @Autowired
    public ServerTransferServiceImpl(TransferDao transferDao, AccountDao accountDao) {
        this.transferDao = transferDao;
        this.accountDao = accountDao;
    }

    @Override
    public void completeTransfer(int accountFromId, int accountToId, double amount) throws AccountNotFoundException {

        if(accountFromId == accountToId){
            throw new IllegalArgumentException("Accounts cannot be the same.");
        }

        Account fromAccount = accountDao.getAccountById(accountFromId);
        if(!hasSufficientFunds(fromAccount, amount)){
            throw new IllegalStateException("Account from balance must be equal to or greater than transfer amount)");
        }

        Account toAccount = accountDao.getAccountById(accountToId);

        Transfer newTransfer = new Transfer();
        newTransfer.set
    }

    private boolean hasSufficientFunds(Account fromAccount, double amount){
        return fromAccount.getBalance() >= amount;
    }

}
