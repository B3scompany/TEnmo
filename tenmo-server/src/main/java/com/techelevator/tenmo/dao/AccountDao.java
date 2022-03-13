package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.AccountNotFoundException;
import com.techelevator.tenmo.exception.UserNotFoundException;
import com.techelevator.tenmo.model.Account;

import java.util.List;

public interface AccountDao {


    public Account getAccountById(int accountId) throws AccountNotFoundException;
    public List<Account> getAllAccountsByUser(int userId) throws AccountNotFoundException;
    public Account update(Account account, int accountId) throws AccountNotFoundException;
    public void delete(int accountId) throws AccountNotFoundException;

}
