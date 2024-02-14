package com.example.aria;

public class UserPass {
    private String username;
    private String password;
    public UserPass(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "UserPass{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
