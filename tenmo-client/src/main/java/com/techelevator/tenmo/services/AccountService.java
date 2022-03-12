package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class AccountService {
    private static final String API_BASE_URL = "http://localhost:8080/";
    private final RestTemplate restTemplate = new RestTemplate();


    // getCurrentBalance(AuthenticatedUser currentUser)
    public double getCurrentBalance(AuthenticatedUser currentUser){
        double balance = 0;
        try{
            ResponseEntity<Double> response =
             restTemplate.exchange(API_BASE_URL + "users/" + currentUser.getUser().getId() + "/accounts/balance",
                     HttpMethod.GET, makeAuthEntity(currentUser), Double.class);
             balance = response.getBody();
        }catch(RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return balance;
    }


    private HttpEntity<Void> makeAuthEntity(AuthenticatedUser currentUser) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(headers);
    }
}
