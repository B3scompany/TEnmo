package com.techelevator.tenmo.model;

public class UserPublicData {

    private int id;
    private String username;

    public int getId() {
        return id;
    }

    public UserPublicData setId(int id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public UserPublicData setUsername(String username) {
        this.username = username;
        return this;
    }
}
