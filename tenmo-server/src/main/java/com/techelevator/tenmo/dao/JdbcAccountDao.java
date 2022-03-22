package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.AccountNotFoundException;
import com.techelevator.tenmo.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcAccountDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Account getAccountById(int accountId) throws AccountNotFoundException {
        String sql = "SELECT account_id, user_id, balance " +
                "FROM account " +
                "WHERE account_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId);
        if(results.next()){
            return mapRowToAccount(results);
        }
        throw new AccountNotFoundException();
    }

    @Override
    public List<Account> getAllAccountsByUser(int userId) throws AccountNotFoundException {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * " +
                "FROM account " +
                "WHERE user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);

        while(results.next()){
            Account account = mapRowToAccount(results);
            accounts.add(account);
        }

        if(accounts.size() == 0){
            throw new AccountNotFoundException("No account found for user id: " + userId);
        }

        return accounts;
    }

    @Override
    public Account update(Account account, int accountId) throws AccountNotFoundException {

        if(account.getAccountId() != accountId){
            throw new IllegalArgumentException("Account Id parameter must match id of Account parameter");
        }

        String sql = "UPDATE account " +
                "SET balance = ?, account_id = ?, user_id = ? " +
                "WHERE account_id = ?;";
        jdbcTemplate.update(sql, account.getBalance(), account.getAccountId(), account.getUserId(),
                accountId);

        return getAccountById(accountId);
    }

    @Override
    public void delete(int accountId) throws AccountNotFoundException {
        String sql = "DELETE FROM account " +
                "WHERE account_id = ?";

        int rowsAffected = jdbcTemplate.update(sql, accountId);

        if(rowsAffected == 0){
            throw new AccountNotFoundException();
        }
    }
    public Account mapRowToAccount(SqlRowSet rowSet){
        Account account = new Account();
        account.setAccountId(rowSet.getInt("account_id"));
        account.setUserId(rowSet.getInt("user_id"));
        account.setBalance(rowSet.getDouble("balance"));

        return account;
    }
}
