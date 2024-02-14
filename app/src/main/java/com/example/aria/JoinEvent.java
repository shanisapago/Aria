package com.example.aria;

import com.example.aria.Alert;

public class JoinEvent {
    private int id;
    private String[] users;
    private Alert[] alert;

    public JoinEvent(String[] users, Alert[] alert, int id) {
        this.users = users;
        this.alert = alert;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String[] getUsername() {
        return users;
    }

    public void setUsername(String[] users) {
        this.users = users;
    }


    public Alert[] getAlert() {
        return alert;
    }

    public void setAlert(Alert[] alert) {
        this.alert = alert;
    }
}
