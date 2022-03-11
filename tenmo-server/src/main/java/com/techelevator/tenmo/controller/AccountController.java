package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.exception.AccountNotFoundException;
import com.techelevator.tenmo.exception.AuthorizationException;
import com.techelevator.tenmo.model.Account;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.techelevator.tenmo.model.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.math.BigDecimal;
import java.util.List;

import java.security.Principal;

@PreAuthorize("isAuthenticated()")
@RestController
public class AccountController {
    private final AccountDao accountDao;
    private final UserDao userDao;

    public AccountController(AccountDao accountDao, UserDao userDao) {
        this.accountDao = accountDao;
        this.userDao = userDao;
    }

    //listUsers() Robert
    @RequestMapping(path = "/users", method = RequestMethod.GET)
    public List<User> listUsers(){
        return userDao.findAll();
    }

    @GetMapping
    @RequestMapping(path = "/users/{userId}/accounts/balance", method = RequestMethod.GET)
    public double getBalance(@PathVariable int userId, Principal principal) throws AccountNotFoundException, AuthorizationException {

        if(principal.getName() != userDao.findByUserId(userId).getUsername()){
            throw new AuthorizationException();
        }

        List<Account> accounts = accountDao.getAllAccountsByUser(userId);

        BigDecimal bigDecimalTotalBalance = BigDecimal.valueOf(0);
        for(Account account: accounts){
            bigDecimalTotalBalance.add(BigDecimal.valueOf(account.getBalance()));
        }
        return bigDecimalTotalBalance.doubleValue();
    }

}
