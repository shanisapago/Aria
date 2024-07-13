package com.example.aria.RetroFitClasses;
import java.util.List;
public class NewAddEvent {
    private String token;
    private String title;
    private String description;
    private String start;
    private String end;
    private String date;
    private String id;
    private String alertString;
    private String flag;


    public NewAddEvent(String id, String token, String title, String description, String start, String end, String alertString, String date, String flag) {
        this.token = token;
        this.id = id;
        this.title = title;
        this.description = description;
        this.start = start;
        this.end = end;
        this.alertString = alertString;
        this.date = date;
        this.flag = flag;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUsername() {
        return token;
    }

    public void setUsername(String token) {
        this.token = token;
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

    public String getAlert() {
        return alertString;
    }

    public void setAlert(String alertString) {
        this.alertString = alertString;
    }

    public String getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAlertString() {
        return alertString;
    }

    public void setAlertString(String alertString) {
        this.alertString = alertString;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
