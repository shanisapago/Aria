package com.example.aria;

public class Alert {
    private String username;
    private String alert;

    public Alert(String username, String alert) {
        this.username = username;
        this.alert = alert;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAlert() {
        return alert;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }
}
