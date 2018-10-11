package com.piyush.pictprint.model;

public class LoginRequest {
    private String username;

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    private String password;

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }
}
