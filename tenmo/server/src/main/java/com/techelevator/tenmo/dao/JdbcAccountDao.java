package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class JdbcAccountDao implements AccountDao{

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Account findAccountByUserId(int userId) {
        String sql = "SELECT account_id, user_id, balance FROM account " +
                "WHERE user_id = ?";
        Account account = new Account();
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        if (results.next()) {
            account = mapRowToAccount(results);
        }
        return account;
    }

    @Override
    public BigDecimal getBalanceByUserId(int userId) {
        Account account = findAccountByUserId(userId);
        return account.getBalance();
    }

    @Override
    public int createAccount(int userId) {
        String sql = "INSERT INTO account (user_id, balance) " +
                "VALUES (?, ?) RETURNING account_id";
        Integer accountId = 0;
        try {
            accountId = jdbcTemplate.queryForObject(sql, Integer.class, userId, 1000);
        } catch (DataAccessException e) {
            System.out.println("Error: could not create account.");
        }
        return accountId;
    }

    @Override
    public boolean adjustBalance (Transfer transfer) {
        boolean success = false;
        Account fromAccount = findAccountByUserId(transfer.getFromUserId());
        Account toAccount = findAccountByUserId(transfer.getToUserId());
        if (fromAccount.getAccountId() != toAccount.getAccountId() && transfer.getTransferAmount().compareTo(new BigDecimal("0.00")) > 0) {
            if (fromAccount.getBalance().subtract(transfer.getTransferAmount()).compareTo(new BigDecimal("0.00")) > 0) {

                String sql = "UPDATE account SET balance = ? WHERE user_id = ?";
                jdbcTemplate.update(sql, fromAccount.getBalance().subtract(transfer.getTransferAmount()), fromAccount.getUserId());

                jdbcTemplate.update(sql, toAccount.getBalance().add(transfer.getTransferAmount()), toAccount.getUserId());
                success = true;
            }
        }
        return success;
    }

    public Account mapRowToAccount(SqlRowSet results){
        Account account = new Account();
        account.setAccountId(results.getInt("account_id"));
        account.setBalance(results.getBigDecimal("balance"));
        account.setUserId(results.getInt("user_id"));
        return account;
    }
}
