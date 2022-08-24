package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {

    List<Transfer> listTransfersByUserId(int userId);

    Transfer getTransferById(int transferId);

    int sendTransfer(int fromUserId, int toUserId, BigDecimal transferAmount);

    Transfer requestTransfer(int fromUserId, int toUserId, BigDecimal transferAmount);

    void updateTransferStatus (int transferId, String status);
}
