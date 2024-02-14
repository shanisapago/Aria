package com.example.aria.RetroFitClasses;

import com.example.aria.Alert;

public class NewEvent {
    private String[] users;
    private String title;
    private String description;
    private String start;
    private String end;
    private String date;
    private Alert[] alert;

    public NewEvent(String[] users, String title, String description, String start, String end, Alert[] alert, String date) {
        this.users = users;
        this.title = title;
        this.description = description;
        this.start = start;
        this.end = end;
        this.alert = alert;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String[] getUsername() {
        return users;
    }

    public void setUsername(String[] users) {
        this.users = users;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public Alert[] getAlert() {
        return alert;
    }

    public void setAlert(Alert[] alert) {
        this.alert = alert;
    }
}
