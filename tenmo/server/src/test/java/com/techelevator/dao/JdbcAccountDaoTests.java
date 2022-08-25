package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

public class JdbcAccountDaoTests extends BaseDaoTests{

    private JdbcAccountDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcAccountDao(jdbcTemplate);
    }

    @Test
    public void findAccountByUserId_pass_in_userId_returns_account(){
        int expectedAccountId = 2001;
        int actualAccountId = sut.findAccountByUserId(1001).getAccountId();
        Assert.assertEquals(expectedAccountId, actualAccountId);
        BigDecimal expectedBalance = new BigDecimal("1000.00");
        BigDecimal actualBalance = sut.findAccountByUserId(1001).getBalance();
        Assert.assertEquals(expectedBalance, actualBalance);
    }

    @Test
    public void getBalanceByUserId_pass_in_userId_returns_balance(){
        BigDecimal expectedBalance = new BigDecimal("1000.00");
        BigDecimal actualBalance = sut.getBalanceByUserId(1001);
        Assert.assertEquals(expectedBalance, actualBalance);
        expectedBalance = new BigDecimal("500.00");
        actualBalance = sut.getBalanceByUserId(1002);
        Assert.assertEquals(expectedBalance, actualBalance);
    }

    @Test
    public void createAccount_pass_in_userId_returns_newAccountId(){
        int expected = 2003;
        int actual = sut.createAccount(1003);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void adjustBalance_pass_in_transfer_returns_true(){
        Transfer transfer = new Transfer(1001, 1002, new BigDecimal("50.00"));
        boolean actual = sut.adjustBalance(transfer);
        Assert.assertTrue(actual);

        BigDecimal expectedBalance = new BigDecimal("950.00");
        BigDecimal actualBalance = sut.getBalanceByUserId(1001);
        Assert.assertEquals(expectedBalance, actualBalance);
        expectedBalance = new BigDecimal("550.00");
        actualBalance = sut.getBalanceByUserId(1002);
        Assert.assertEquals(expectedBalance, actualBalance);
    }

    @Test
    public void adjustBalance_pass_in_invalidTransfer_returns_false(){
        Transfer transfer = new Transfer(1002, 1001, new BigDecimal("600.00"));
        boolean actual = sut.adjustBalance(transfer);
        Assert.assertFalse(actual);

        transfer = new Transfer(1001, 1001, new BigDecimal("600.00"));
        actual = sut.adjustBalance(transfer);
        Assert.assertFalse(actual);
    }


}
