package com.example.aria.RetroFitClasses;

public class MembersNotificationsMsg {
    private String id;
    private String message;
    private  String appToken;
    private String tokenSender;
    private String name;
    private String title;

    public MembersNotificationsMsg(String id, String message, String appToken, String tokenSender, String name, String title) {
        this.id = id;
        this.message = message;
        this.appToken = appToken;
        this.tokenSender = tokenSender;
        this.name = name;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAppToken() {
        return appToken;
    }

    public void setAppToken(String appToken) {
        this.appToken = appToken;
    }

    public String getTokenSender() {
        return tokenSender;
    }

    public void setTokenSender(String tokenSender) {
        this.tokenSender = tokenSender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}