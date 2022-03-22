package com.techelevator;

import com.techelevator.tenmo.dao.*;
import com.techelevator.tenmo.exception.AccountNotFoundException;
import com.techelevator.tenmo.exception.TransferNotFoundException;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.UserPublicData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class JdbcTransferDaoTest extends BaseDaoTest  {

    private static final Transfer TRANSFER_1 =
            new Transfer(3001,
                    "Send",
                    "Approved",
                    new UserPublicData(1001, "a"),
                    new UserPublicData(1002, "b"),
                    11.11);

    private static final Transfer TRANSFER_2 =
            new Transfer(3002,
                    "Request",
                    "Pending",
                    new UserPublicData(1002, "b"),
                    new UserPublicData(1003, "c"),
                    22.22);

    private static final Transfer TRANSFER_3 =
            new Transfer(3003,
                    "Request",
                    "Rejected",
                    new UserPublicData(1001, "a"),
                    new UserPublicData(1003, "c"),
                    33.33);

    private JdbcTransferDao sut;

    @Before
    public void setup() {
        AccountDao accountDao = new JdbcAccountDao(new JdbcTemplate(dataSource));
        sut = new JdbcTransferDao(new JdbcTemplate(dataSource), accountDao);
    }

    @Test
    public void getTransferById_returns_correct_transfer_for_id() throws AccountNotFoundException, TransferNotFoundException {
        // Arrange

        // Act
        Transfer actualTransfer1 = sut.getTransferById(3001);

        // Assert
        assertTransfersMatch(TRANSFER_1, actualTransfer1);

        // Act
        Transfer actualTransfer2 = sut.getTransferById(3002);

        // Assert
        assertTransfersMatch(TRANSFER_2, actualTransfer2);
    }

    @Test
    public void getAllTransfersByUser_returns_all_transfers_for_user() throws AccountNotFoundException {
        List<Transfer> actualTransfers = sut.getAllTransfersForUser(1001);
        Assert.assertEquals(2, actualTransfers.size());
        assertTransfersMatch(TRANSFER_1, actualTransfers.get(0));
        assertTransfersMatch(TRANSFER_3, actualTransfers.get(1));
    }

    @Test
    public void getAllTransfersForUser_returns_empty_list_if_no_transfers_found_for_user() {
        Assert.assertEquals(0, sut.getAllTransfersForUser(1004).size());
    }

    @Test
    public void updated_transfer_has_expected_values_when_retrieved() throws AccountNotFoundException, TransferNotFoundException {

        Transfer transferToUpdate = sut.getTransferById(3001);

        transferToUpdate.setAmount(525.25);
        transferToUpdate.setTransferStatus("Approved");

        sut.updateTransfer(transferToUpdate, 3001);

        Transfer retrievedTransfer = sut.getTransferById(3001);
        assertTransfersMatch(transferToUpdate, retrievedTransfer);
    }

    private void assertTransfersMatch(Transfer expected, Transfer actual) {
        Assert.assertEquals(expected.getFromUser().getId(), actual.getFromUser().getId());
        Assert.assertEquals(expected.getFromUser().getUsername(), actual.getFromUser().getUsername());
        Assert.assertEquals(expected.getToUser().getId(), actual.getToUser().getId());
        Assert.assertEquals(expected.getToUser().getUsername(), actual.getToUser().getUsername());
        Assert.assertEquals(expected.getAmount(), actual.getAmount(), 0);
        Assert.assertEquals(expected.getTransferStatus(), actual.getTransferStatus());
        Assert.assertEquals(expected.getTransferType(), actual.getTransferType());
    }
}
