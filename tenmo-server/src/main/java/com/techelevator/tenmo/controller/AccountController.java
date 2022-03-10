package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.exception.AccountNotFoundException;
import com.techelevator.tenmo.exception.AuthorizationException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
public class AccountController {
    private final AccountDao accountDao;
    private final TransferDao transferDao;
    private final UserDao userDao;

    public AccountController(AccountDao accountDao, TransferDao transferDao, UserDao userDao) {
        this.accountDao = accountDao;
        this.transferDao = transferDao;
        this.userDao = userDao;
    }

    //listUsers() Robert



    //getBalance(account_id) Scott
    @GetMapping
    @RequestMapping(path = "/accounts/{id}/balance", method = RequestMethod.GET)
    public double getBalance(@PathVariable int id, Principal principal) throws AccountNotFoundException, AuthorizationException {
        Account account = accountDao.getAccountById(id);
        if(!principal.getName().equals(account.getAccountUser().getUsername())){
            throw new AuthorizationException();
        }
        return account.getBalance();
    }

}
