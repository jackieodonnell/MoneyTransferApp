package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.Exception.TransferNotFoundException;
import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.RequestDTO;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
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
    public Transfer sendTransfer(Principal principal, @RequestBody TransferDTO transferDTO) throws TransferNotFoundException{
        int fromUserId = userDao.findIdByUsername(principal.getName());
        int transferId = transferDao.sendTransfer(fromUserId, transferDTO.getToUserId(), transferDTO.getTransferAmount());
        return transferDao.getTransferById(transferId);
    }

    @RequestMapping(path = "/transfer/request", method = RequestMethod.POST)
    public Transfer requestTransfer(Principal principal, @RequestBody TransferDTO transferDTO) throws TransferNotFoundException {
        int toUserId = userDao.findIdByUsername(principal.getName());
        int transferId = transferDao.requestTransfer(transferDTO.getToUserId(), toUserId, transferDTO.getTransferAmount());
        return transferDao.getTransferById(transferId);
    }

    @RequestMapping(path = "/transfer/{transferId}", method = RequestMethod.POST)
    public String approveTransfer(Principal principal, @PathVariable int transferId, @RequestBody RequestDTO requestDTO) throws TransferNotFoundException {
        Transfer transfer = transferDao.getTransferById(transferId);
        if (transfer.getFromUserId() == userDao.findIdByUsername(principal.getName())) {
            // boolean approved = isApproved.equalsIgnoreCase("true");
            boolean success = transferDao.approveTransfer(transferId, requestDTO.getIsApproved());
            if (success) {
                return "Transfer Approved!";
            } else {
                return "Transfer Not Approved!";
            }
        } else {
            throw new DataAccessException("user does not have access to this transfer!"){};
        }
    }

    @RequestMapping(path = "/transfer/history", method = RequestMethod.GET)
    public List<Transfer> listTransfersByUserId(Principal principal){
        int userId = userDao.findIdByUsername(principal.getName());
        return transferDao.listTransfersByUserId(userId);
    }

    @RequestMapping(path = "/transfer/pending", method = RequestMethod.GET)
    public List<Transfer> listPendingByUserId(Principal principal){
        int userId = userDao.findIdByUsername(principal.getName());
        return transferDao.listPendingByUserId(userId);
    }


    @ResponseStatus(HttpStatus.NOT_FOUND)
    @RequestMapping(path = "/transfer/{transferId}", method = RequestMethod.GET)
    public Transfer getTransferDetailsById(Principal principal, @PathVariable int transferId) throws TransferNotFoundException  {
        Transfer transfer = transferDao.getTransferById(transferId);
        int principalId = userDao.findIdByUsername(principal.getName());
        int fromUserId = transfer.getFromUserId();
        int toUserId = transfer.getToUserId();

        if (principalId == toUserId || principalId == fromUserId) {
            return transfer;
        } else {
            throw new DataAccessException("user does not have access to this transfer!"){};
        }


    }


}
