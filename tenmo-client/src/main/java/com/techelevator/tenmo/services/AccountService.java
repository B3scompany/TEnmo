package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class AccountService {
    private static final String API_BASE_URL = "http://localhost:8080/accounts";
    private final RestTemplate restTemplate = new RestTemplate();


    // getCurrentBalance(AuthenticatedUser currentUser)


    private HttpEntity<Void> makeAuthEntity(AuthenticatedUser currentUser) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(headers);
    }
}
