package com.techelevator;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.exception.AccountNotFoundException;
import com.techelevator.tenmo.model.Account;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class JdbcAccountDAOTest extends BaseDaoTest{


    private static final Account ACCOUNT_1 = new Account().setAccountId(2001).setUserId(1001).setBalance(1000);
    private static final Account ACCOUNT_2 = new Account().setAccountId(2002).setUserId(1002).setBalance(2000);
    private static final Account ACCOUNT_3 = new Account().setAccountId(2003).setUserId(1003).setBalance(3000);

    private JdbcAccountDao sut;

    @Before
    public void setup() {
        UserDao userDao = new JdbcUserDao(new JdbcTemplate(dataSource));
        sut = new JdbcAccountDao(new JdbcTemplate(dataSource), userDao);
    }

    @Test
    public void getAccount_returns_correct_account_for_id() throws AccountNotFoundException {
        // Arrange

        // Act
        Account accountActual1 = sut.getAccountById(2001);

        // Assert
        assertAccountsMatch(ACCOUNT_1, accountActual1);

        // Act
        Account accountActual2 = sut.getAccountById(2002);

        // Assert
        assertAccountsMatch(ACCOUNT_2, accountActual2);
    }

    @Test(expected = AccountNotFoundException.class)
    public void getAccount_throws_exception_when_id_not_found() throws AccountNotFoundException {
        sut.getAccountById(20000);
    }

    @Test
    public void getAllAccountsByUser_returns_all_accounts_for_user() throws AccountNotFoundException {
        List<Account> actualAccounts = sut.getAllAccountsByUser(1001);
        Assert.assertEquals(1, actualAccounts.size());
        assertAccountsMatch(ACCOUNT_1, actualAccounts.get(0));
    }

    @Test(expected = AccountNotFoundException.class)
    public void getAllAccountsByUser_throws_exception_when_no_accounts_found_for_user() throws AccountNotFoundException {
        sut.getAllAccountsByUser(999);
    }

    @Test
    public void updated_account_has_expected_values_when_retrieved() throws AccountNotFoundException {

        Account accountToUpdate = sut.getAccountById(2001);

        accountToUpdate.setUserId(1003);
        accountToUpdate.setBalance(4000);

        sut.update(accountToUpdate, 2001);

        Account retrievedAccount = sut.getAccountById(2001);
        assertAccountsMatch(accountToUpdate, retrievedAccount);
    }

    private void assertAccountsMatch(Account expected, Account actual) {
        Assert.assertEquals(expected.getAccountId(), actual.getAccountId());
        Assert.assertEquals(expected.getUserId(), actual.getUserId());
        Assert.assertEquals(expected.getBalance(), actual.getBalance(), 0);
    }
}
