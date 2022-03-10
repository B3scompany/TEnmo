package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.TransferNotFoundException;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {

    private final JdbcTemplate jdbcTemplate;
    private final AccountDao accountDao;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate, AccountDao accountDao){
        this.jdbcTemplate = jdbcTemplate;
        this.accountDao = accountDao;
    }

    @Override
    public Transfer create(Transfer transfer) throws TransferNotFoundException {
        String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) VALUES(?, ?, ?, ?, ?) RETURNING transfer_id;";

        int newId = jdbcTemplate.queryForObject(sql, Integer.class,
                getTransferTypeIdByDesc(transfer.getTransferType()),
                getTransferStatusIdByDesc(transfer.getTransferStatus()),
                transfer.getAccountFromId(),
                transfer.getAccountToId(),
                transfer.getAmount());

        return getTransferById(newId);
    }

    @Override
    public Transfer getTransferById(int transferId) throws TransferNotFoundException {

        String sql = "SELECT transfer_status_desc, transfer_type_desc, transfer_status_id, account_from, account_to, amount FROM transfer " +
                "JOIN transfer_status USING (transfer_status_id) " +
                "JOIN transfer_type USING(transfer_type_id) " +
                "WHERE transfer_id = ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);

        if(results.next()) {
            return mapRowToTransfer(results);
        }

        throw new TransferNotFoundException();
    }

    @Override
    public List<Transfer> getAllTransfersForUser(int userId) {
        String sql = "SELECT transfer_status_desc, transfer_type_desc, transfer_status_id, account_from, account_to, amount FROM transfer " +
                "JOIN tenmo_user USING(user_id) " +
                "JOIN transfer_status USING (transfer_status_id) " +
                "JOIN transfer_type USING(transfer_type_id) " +
                "WHERE account_from = ? OR account_to = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, userId);

        List<Transfer> allTransfersForUser = new ArrayList<>();

        while(results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            if(transfer != null){
                allTransfersForUser.add(transfer);
            }
        }

        return allTransfersForUser;
    }

    @Override
    public Transfer updateTransfer(Transfer transfer, int transferId) throws TransferNotFoundException {
        String sql = "UPDATE transfer " +
                "SET transfer_id = ?," +
                "transfer_type_id = ?," +
                "transfer_status_id = ?," +
                "account_from = ?," +
                "account_to = ? ," +
                "amount = ? " +
                "WHERE transfer_id = ?;";

        jdbcTemplate.update(sql,
                transfer.getTransferId(),
                getTransferTypeIdByDesc(transfer.getTransferType()),
                getTransferStatusIdByDesc(transfer.getTransferStatus()),
                transfer.getAccountFromId(),
                transfer.getAccountToId(),
                transfer.getAmount(),
                transferId);

        return getTransferById(transferId);
    }

    @Override
    public boolean deleteTransferById(int transferId) throws TransferNotFoundException {
        String sql = "DELETE FROM transfer WHERE transfer_id = ?;";

        int rowsAffected = jdbcTemplate.update(sql, transferId);

        if (rowsAffected == 0) throw new TransferNotFoundException();

        return true;
    }

    private Transfer mapRowToTransfer(SqlRowSet row) {
        Transfer transfer = new Transfer(accountDao);
        transfer.setTransferId(row.getInt("transfer_id"));
        transfer.setTransferType(row.getString("transfer_type_desc"));
        transfer.setTransferStatus(row.getString("transfer_status_id"));
        transfer.setAccountFromId(row.getInt("account_from"));
        transfer.setAccountToId(row.getInt("account_to"));
        transfer.setAmount(row.getDouble("amount"));
        return transfer;
    }


    private int getTransferStatusIdByDesc(String transferStatusDesc){
        String sql = "SELECT transfer_status_id FROM transfer_status WHERE transfer_status_desc = ?;";
        Integer transferStatusId = jdbcTemplate.queryForObject(sql, Integer.class, transferStatusDesc);
        if(transferStatusId == null){
            throw new RuntimeException("Transfer Type: " + transferStatusDesc + " not found.");
        }
        return transferStatusId;
    }

    private int getTransferTypeIdByDesc(String transferTypeDesc){
        String sql = "SELECT transfer_type_id FROM transfer_type WHERE transfer_type_desc = ?;";
        Integer transferTypeId = jdbcTemplate.queryForObject(sql, Integer.class, transferTypeDesc);

        if(transferTypeId == null){
            throw new RuntimeException("Transfer Type: " + transferTypeDesc + " not found.");
        }

        return transferTypeId;
    }

}
