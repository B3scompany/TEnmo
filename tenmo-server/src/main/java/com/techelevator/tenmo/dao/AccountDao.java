package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.UserNotFoundException;
import com.techelevator.tenmo.model.Account;

import java.util.List;

public interface AccountDao {

    public Account created(Account account, int userID) throws UserNotFoundException;
    public Account getAccountById(int accountId);
    public List<Account> getAllAccountsByUser(int userId);
    public Account update(Account account, int accountId);
    public Account delete(int accountId);

}
