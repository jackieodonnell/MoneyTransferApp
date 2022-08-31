package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.security.Principal;

@RestController
@PreAuthorize("isAuthenticated()")
public class AccountController {

    @Autowired
    private UserDao userDao;
    @Autowired
    private AccountDao accountDao;

    @RequestMapping(path = "/balance", method = RequestMethod.GET)
    public BigDecimal getBalanceByUserId(Principal principal){
        int userId = userDao.findIdByUsername(principal.getName());
        return accountDao.getBalanceByUserId(userId);
    }

}
