package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class TransferController {

    @Autowired
    TransferDao transferDao;

    @Autowired
    UserDao userDao;

    @Autowired
    AccountDao accountDao;

    @RequestMapping(path = "/transfer", method = RequestMethod.POST)
    public Transfer sendTransfer(Principal principal, @RequestBody TransferDTO transferDTO){
        int fromUserId = userDao.findIdByUsername(principal.getName());
        int transferId = transferDao.sendTransfer(fromUserId, transferDTO.getToUserId(), transferDTO.getTransferAmount());
        return transferDao.getTransferById(transferId);
    }

    @RequestMapping(path = "/transfer/history", method = RequestMethod.GET)
    public List<Transfer> listTransfersByUserId(Principal principal){
        int userId = userDao.findIdByUsername(principal.getName());
        return transferDao.listTransfersByUserId(userId);
    }


}
