package com.example.aria.RetroFitClasses;

public class PhoneUsers {
    private String[] users;
    private String[] notUsers;

    public PhoneUsers (String[] users, String[] notUsers){
        this.notUsers = notUsers;
        this.users = users;
    }

    public void setNotUsers(String[] notUsers) {
        this.notUsers = notUsers;
    }

    public void setUsers(String[] users) {
        this.users = users;
    }

    public String[] getUsers() {
        return users;
    }

    public String[] getNotUsers() {
        return notUsers;
    }
}
