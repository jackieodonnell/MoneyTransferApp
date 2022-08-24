package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {

    List<Transfer> listTransfersById(int userId);

    Transfer getTransferById(int transferId);

    int sendTransfer(int fromUserId, int toUserId, BigDecimal transferAmount);

    Transfer requestTransfer(int fromUserId, int toUserId, BigDecimal transferAmount);

    boolean updateTransferStatus (int transferId, String status);
}
