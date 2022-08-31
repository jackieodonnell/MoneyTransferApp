package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.Exception.TransferNotFoundException;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.dao.DataAccessException;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {

    List<Transfer> listTransfersByUserId(int userId);

    Transfer getTransferById(int transferId) throws TransferNotFoundException;

    int sendTransfer(int fromUserId, int toUserId, BigDecimal transferAmount) throws TransferNotFoundException;

    int requestTransfer(int fromUserId, int toUserId, BigDecimal transferAmount) throws TransferNotFoundException, DataAccessException;

    void updateTransferStatus (int transferId, String status);

    boolean approveTransfer(int transferId, boolean isApproved) throws TransferNotFoundException;

    List<Transfer> listPendingByUserId(int userId);
}
