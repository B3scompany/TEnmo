package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserPublicData;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class TransferService {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String API_BASE_URL = "http://localhost:8080/";

    public List<Transfer> transferHistory(AuthenticatedUser currentUser){
        List<Transfer> transferHistory = null;
        try{
            ResponseEntity<Transfer[]> response =
                    restTemplate.exchange(API_BASE_URL + "transfers", HttpMethod.GET,
                            makeAuthEntity(currentUser), Transfer[].class);
            Transfer[] transfers = response.getBody();
            transferHistory = new ArrayList<>(List.of(transfers));
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transferHistory;

    }

    public Transfer submitSendTransfer(Transfer transfer, AuthenticatedUser currentUser){
        Transfer result = null;
        try {
            ResponseEntity<Transfer> response =
                    restTemplate.exchange(API_BASE_URL + "transfers",
                            HttpMethod.POST, makeAuthEntityWithTransfer(transfer, currentUser), Transfer.class);
            result = response.getBody();
            BasicLogger.log("Test");
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return result;
    }

    public Transfer transferOf(User fromUser, User toUser, double amount, String transferType, String transferStatus){
        Transfer result = new Transfer();
                result.setFromUser(new UserPublicData().setUsername(fromUser.getUsername()).setId(fromUser.getId().intValue()));
                result.setToUser(new UserPublicData().setUsername(toUser.getUsername()).setId(toUser.getId().intValue()));
                result.setAmount(amount);
                result.setTransferType(transferType);
                result.setTransferStatus(transferStatus);
        return result;
    }
    public String sendOrReceive(Transfer transfer, AuthenticatedUser currentUser){
        if(transfer.getFromUser().getId() == currentUser.getUser().getId()){
            return "To: " + transfer.getToUser().getUsername();
        }
        return "From: " + transfer.getFromUser().getUsername();
    }

    public Transfer getTransferDetails(int transferId){
        Transfer transfer = null;
        try{
            transfer = restTemplate.getForObject(API_BASE_URL + "transfers/" + transferId,
                     Transfer.class);
        }catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transfer;

    }

    private HttpEntity<Void> makeAuthEntity(AuthenticatedUser currentUser) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(headers);
    }

    private HttpEntity<Transfer> makeAuthEntityWithTransfer(Transfer transfer, AuthenticatedUser currentUser) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(transfer, headers);
    }



}
