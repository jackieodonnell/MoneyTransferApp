package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
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
        String sql = "SELECT transfer_id, from_user_id, to_user_id, amount, status FROM transfer WHERE from_user_id = ? OR to_user_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, userId);
        List<Transfer> transferList = new ArrayList<>();
        while(results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            transferList.add(transfer);
        }
        return transferList;
    }

    @Override
    public Transfer getTransferById(int transferId) {
        String sql = "SELECT transfer_id, from_user_id, to_user_id, amount, status FROM transfer WHERE transfer_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
        Transfer transfer = new Transfer();
        if (results.next()) {
             transfer = mapRowToTransfer(results);
        }
        return transfer;
    }

    @Override
    public int sendTransfer(int fromUserId, int toUserId, BigDecimal transferAmount) {
        String sql = "INSERT INTO transfer (from_user_id, to_user_id, amount, status) VALUES (?,?,?,?) RETURNING transfer_id";
        int transferId = jdbcTemplate.queryForObject(sql, Integer.class, fromUserId, toUserId, transferAmount, "Approved");
        Transfer transfer = getTransferById(transferId);

        // check if there's enough money, check not sending to self, decrease from balance, increase to balance,
        boolean success = jdbcAccountDao.adjustBalance(transfer);
        if (!success) {
            updateTransferStatus(transferId, "Rejected");
        }
        return transferId;
    }

    @Override
    public Transfer requestTransfer(int fromUserId, int toUserId, BigDecimal transferAmount) {
        return null;
    }

    @Override
    public void updateTransferStatus(int transferId, String status) {
        String sql = "UPDATE transfer SET status = ? WHERE transfer_id = ?";
        jdbcTemplate.update(sql, status, transferId);

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
