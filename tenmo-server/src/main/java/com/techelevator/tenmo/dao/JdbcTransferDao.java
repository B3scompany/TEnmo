package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.AccountNotFoundException;
import com.techelevator.tenmo.exception.TransferNotFoundException;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.UserPublicData;
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
    public Transfer create(Transfer transfer) throws TransferNotFoundException, AccountNotFoundException {
        String sql = "INSERT INTO transfer " +
                "(transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES(?, ?, ?, ?, ?) RETURNING transfer_id;";

        int newId = jdbcTemplate.queryForObject(sql, Integer.class,
                getTransferTypeIdByDesc(transfer.getTransferType()),
                getTransferStatusIdByDesc(transfer.getTransferStatus()),
                accountDao.getAllAccountsByUser(transfer.getFromUser().getId()).get(0).getAccountId(),
                accountDao.getAllAccountsByUser(transfer.getToUser().getId()).get(0).getAccountId(),
                transfer.getAmount());

        return getTransferById(newId);
    }

    @Override
    public Transfer getTransferById(int transferId) throws TransferNotFoundException {

        String sql =

                "SELECT from_username, to_username, transfer_id, transfer_status_desc, transfer_type_desc, from_user_id, to_user_id, amount \n" +
                "FROM \n" +
                "(SELECT username as from_username, user_id as from_user_id, account_id as from_account FROM account JOIN tenmo_user USING(user_id)) account_from_data\n" +
                "JOIN transfer ON transfer.account_from = account_from_data.from_account\n" +
                "JOIN (SELECT username as to_username, user_id as to_user_id, account_id as to_account FROM account JOIN tenmo_user USING(user_id)) to_account_data ON to_account = transfer.account_to \n" +
                " JOIN transfer_status USING (transfer_status_id)\n" +
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
        String sql =

                "SELECT from_username, to_username, transfer_id, transfer_status_desc, transfer_type_desc, from_user_id, to_user_id, amount \n" +
                "FROM \n" +
                "(SELECT username as from_username, user_id as from_user_id, account_id as from_account FROM account JOIN tenmo_user USING(user_id)) account_from_data\n" +
                "JOIN transfer ON transfer.account_from = account_from_data.from_account\n" +
                "JOIN (SELECT username as to_username, user_id as to_user_id, account_id as to_account FROM account JOIN tenmo_user USING(user_id)) to_account_data ON to_account = transfer.account_to \n" +
                " JOIN transfer_status USING (transfer_status_id)\n" +
                "JOIN transfer_type USING(transfer_type_id) " +
                "WHERE from_user_id = ? OR to_user_id = ?;";

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
    public Transfer updateTransfer(Transfer transfer, int transferId) throws TransferNotFoundException, AccountNotFoundException {

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
                accountDao.getAllAccountsByUser(transfer.getFromUser().getId()).get(0).getAccountId(),
                accountDao.getAllAccountsByUser(transfer.getToUser().getId()).get(0).getAccountId(),
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
        Transfer transfer = new Transfer();
        transfer.setTransferId(row.getInt("transfer_id"));
        transfer.setTransferType(row.getString("transfer_type_desc"));
        transfer.setTransferStatus(row.getString("transfer_status_desc"));
        transfer.setFromUser(mapRowToFromUserPublicData(row));
        transfer.setToUser(mapRowToToUserPublicData(row));
        transfer.setAmount(row.getDouble("amount"));
        return transfer;
    }

    private UserPublicData mapRowToFromUserPublicData(SqlRowSet row){
        UserPublicData user = new UserPublicData()
                .setUsername(row.getString("from_username"))
                .setId(row.getInt("from_user_id"));
        return user;
    }

    private UserPublicData mapRowToToUserPublicData(SqlRowSet row){
        UserPublicData user = new UserPublicData()
                .setUsername(row.getString("to_username"))
                .setId(row.getInt("to_user_id"));
        return user;
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
