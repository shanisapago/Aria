package com.example.aria.RetroFitClasses;

public class PhoneUsers2 {
    private String id;
    private NotUser [] notUsers;
    private String appToken;
    private String sender;
    private String title;

    public PhoneUsers2(String id, NotUser[] notUsers, String appToken, String sender, String title) {
        this.id = id;
        this.notUsers = notUsers;
        this.appToken = appToken;
        this.sender = sender;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public NotUser[] getNotUsers() {
        return notUsers;
    }

    public void setNotUsers(NotUser[] notUsers) {
        this.notUsers = notUsers;
    }

    public String getAppToken() {
        return appToken;
    }

    public void setAppToken(String appToken) {
        this.appToken = appToken;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}