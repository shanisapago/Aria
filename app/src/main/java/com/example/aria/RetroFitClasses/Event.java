package com.example.aria.RetroFitClasses;

import com.example.aria.Alert;

public class Event {
    private String username;
    private int id;
    private String title;
    private String description;
    private String start;
    private String end;
    private Alert[] alert;

    public Event(String username, int id, String title, String description, String start, String end, Alert[] alert) {
        this.username = username;
        this.id = id;
        this.title = title;
        this.description = description;
        this.start = start;
        this.end = end;
        this.alert = alert;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
