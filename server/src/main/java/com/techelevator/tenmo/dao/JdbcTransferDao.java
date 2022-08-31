package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.Exception.TransferNotFoundException;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao{

    private JdbcAccountDao jdbcAccountDao;

    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate, JdbcAccountDao jdbcAccountDao){
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcAccountDao = jdbcAccountDao;
    }


    @Override
    public List<Transfer> listTransfersByUserId(int userId) {
        String sql = "SELECT transfer_id, from_user_id, to_user_id, amount, status " +
                "FROM transfer WHERE from_user_id = ? OR to_user_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, userId);
        List<Transfer> transferList = new ArrayList<>();
        while(results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            transferList.add(transfer);
        }
        return transferList;
    }

    @Override
    public List<Transfer> listPendingByUserId(int userId) {
        String sql =  "SELECT transfer_id, from_user_id, to_user_id, amount, status " +
                "FROM transfer WHERE status = ? AND (from_user_id = ? OR to_user_id = ?)";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, "Pending", userId, userId);
        List<Transfer> pendingList = new ArrayList<>();
        while(results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            pendingList.add(transfer);
        }
        return pendingList;
    }


    @Override
    public Transfer getTransferById(int transferId) throws TransferNotFoundException {
        String sql = "SELECT transfer_id, from_user_id, to_user_id, amount, status " +
                "FROM transfer WHERE transfer_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
        Transfer transfer = new Transfer();
        if (results.next()) {
             transfer = mapRowToTransfer(results);
        } else {
            throw new TransferNotFoundException();
        }
        return transfer;
    }

    @Override
    public int sendTransfer(int fromUserId, int toUserId, BigDecimal transferAmount) throws TransferNotFoundException {
        String sql = "INSERT INTO transfer (from_user_id, to_user_id, amount, status) " +
                "VALUES (?,?,?,?) RETURNING transfer_id";
        int transferId = jdbcTemplate.queryForObject(sql, Integer.class, fromUserId, toUserId, transferAmount, "Approved");
        Transfer transfer = getTransferById(transferId);

        // adjustBalance() verifies sender has sufficient funds, is sending to a different user, and
        // decreases sender account balance/increases recipient account balance by transfer amount
        boolean success = jdbcAccountDao.adjustBalance(transfer);
        if (!success) {
            updateTransferStatus(transferId, "Rejected");
        }
        return transferId;
    }

    @Override
    public int requestTransfer(int fromUserId, int toUserId, BigDecimal transferAmount) throws TransferNotFoundException, DataAccessException {
        int transferId = -1;
        if ((fromUserId == toUserId) || (transferAmount.compareTo(new BigDecimal("0.00")) <= 0)) {
            throw new DataAccessException("Invalid transfer request!") {};
        } else {
            String sql = "INSERT INTO transfer (from_user_id, to_user_id, amount, status) " +
                    "VALUES (?,?,?,?) RETURNING transfer_id";
            transferId = jdbcTemplate.queryForObject(sql, Integer.class, fromUserId, toUserId, transferAmount, "Pending");
            Transfer transfer = getTransferById(transferId);
        }
        return transferId;
    }

    @Override
    public void updateTransferStatus(int transferId, String status) {
        String sql = "UPDATE transfer SET status = ? WHERE transfer_id = ?";
        jdbcTemplate.update(sql, status, transferId);

    }

    @Override
    public boolean approveTransfer(int transferId, boolean isApproved) throws TransferNotFoundException {
        boolean success = false;
        Transfer transfer = getTransferById(transferId);
        if (isApproved && transfer.getStatus().equals("Pending")) {
            success = jdbcAccountDao.adjustBalance(transfer);
            if (success) {
                updateTransferStatus(transferId, "Approved");
            }
        } else {
            updateTransferStatus(transferId, "Rejected");
        }
        return success;
    }

    private Transfer mapRowToTransfer(SqlRowSet results) {
        Transfer transfer = new Transfer();
        transfer.setTransferAmount(results.getBigDecimal("amount"));
        transfer.setFromUserId(results.getInt("from_user_id"));
        transfer.setToUserId(results.getInt("to_user_id"));
        transfer.setStatus(results.getString("status"));
        transfer.setTransferId(results.getInt("transfer_id"));
        return transfer;
    }
}
