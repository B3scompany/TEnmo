package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.Account;
import org.springframework.web.client.RestTemplate;

public class AccountService {
    private static final String API_BASE_URL = "http://localhost:8080/accounts";
    private final RestTemplate restTemplate = new RestTemplate();



}
