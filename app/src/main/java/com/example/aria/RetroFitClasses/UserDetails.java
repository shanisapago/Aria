package com.example.aria.RetroFitClasses;

public class UserDetails {
    private String token;
    private String fullName;

    public UserDetails(String token, String fullName) {
        this.token = token;
        this.fullName = fullName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
