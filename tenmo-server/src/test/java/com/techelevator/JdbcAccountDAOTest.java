package com.techelevator;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.model.Account;
import org.junit.Before;
import org.springframework.jdbc.core.JdbcTemplate;

public class JdbcAccountDAOTest extends BaseDaoTest{

    private JdbcAccountDao sut;
    private Account testAccount;
    @Before
    public void setup(){
        sut = new JdbcAccountDao(dataSource);
    }

}
