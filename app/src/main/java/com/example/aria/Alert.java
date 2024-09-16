package com.example.aria;

public class Alert {
    private String username;
    private String alert;
    private int requestCode;

    public Alert(String username, String alert, int requestCode) {
        this.username = username;
        this.alert = alert;
        this.requestCode = requestCode;
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

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }
}