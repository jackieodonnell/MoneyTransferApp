package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;

public interface AccountDao {

    Account findAccountByUserId(int userId);

    BigDecimal getBalanceByUserId(int userId);

    int createAccount(int userId);

    public boolean adjustBalance (Transfer transfer);

}
