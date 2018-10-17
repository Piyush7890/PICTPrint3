package com.piyush.pictprint.model;

public class LoginRequest {
    private String email;
    private String username;

    public LoginRequest(String email, String username, String password) {
        this.email = email;
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
