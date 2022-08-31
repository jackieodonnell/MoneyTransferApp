package com.techelevator.dao;

import com.techelevator.tenmo.Exception.TransferNotFoundException;
import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcTransferDao;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

public class JdbcTransferDaoTests extends BaseDaoTests{

    private JdbcTransferDao sut;
    private JdbcAccountDao jdbcAccountDao;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcTransferDao(jdbcTemplate, jdbcAccountDao);
    }

    @Test
    public void listTransfersByUserId_returns_list(){
        int expected = 1;
        int actual = sut.listTransfersByUserId(1001).size();
        Assert.assertEquals(expected, actual);
        actual = sut.listTransfersByUserId(1002).size();
        Assert.assertEquals(expected, actual);

        expected = 0;
        actual = sut.listTransfersByUserId(1003).size();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getTransferById_pass_in_transferId_returns_Transfer() throws TransferNotFoundException {
        BigDecimal expectedAmount = new BigDecimal("100.00");
        BigDecimal actualAmount = sut.getTransferById(3001).getTransferAmount();
        Assert.assertEquals(expectedAmount, actualAmount);
    }

/*    @Test
    public void sendTransfer_pass_in_args_returns_transferId() throws TransferNotFoundException {
        int expected = 3002;
        int actual = sut.sendTransfer(1001,1002, new BigDecimal("75.00"));
        Assert.assertEquals(expected, actual);
    }*/

    @Test
    public void updateTransferStatus_pass_in_new_status_get_new_status() throws TransferNotFoundException {
        sut.updateTransferStatus(3001, "Rejected");
        String expected = "Rejected";
        String actual = sut.getTransferById(3001).getStatus();
        Assert.assertEquals(expected, actual);
    }

}
