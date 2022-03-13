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

    public Transfer submitTransfer(Transfer transfer, AuthenticatedUser currentUser){
        Transfer result = null;
        try {
            ResponseEntity<Transfer> response =
                    restTemplate.exchange(API_BASE_URL + "transfers",
                            HttpMethod.POST, makeAuthEntityWithTransfer(transfer, currentUser), Transfer.class);
            result = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return result;
    }

    public Transfer transferOf(User fromUser, User toUser, double amount, String transferType, String transferStatus){
        Transfer result = new Transfer()
                .setFromUser(new UserPublicData().setUsername(fromUser.getUsername()).setId(fromUser.getId().intValue()))
                .setToUser(new UserPublicData().setUsername(toUser.getUsername()).setId(toUser.getId().intValue()))
                .setAmount(amount)
                .setTransferType(transferType)
                .setTransferStatus(transferStatus);
        return result;
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
