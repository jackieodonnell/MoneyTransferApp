package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {
    private int transferId;
    private int fromUserId;
    private int toUserId;
    private BigDecimal transferAmount;
    private String status;

    public Transfer() {
    }

    public Transfer(int fromUserId, int toUserId, BigDecimal transferAmount) {
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.transferAmount = transferAmount;
    }

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public int getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(int fromUserId) {
        this.fromUserId = fromUserId;
    }

    public int getToUserId() {
        return toUserId;
    }

    public void setToUserId(int toUserId) {
        this.toUserId = toUserId;
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Transfer{" +
                "transferId=" + transferId +
                ", fromUserId=" + fromUserId +
                ", toUserId=" + toUserId +
                ", amount=" + transferAmount +
                ", status='" + status + '\'' +
                '}';
    }
}
