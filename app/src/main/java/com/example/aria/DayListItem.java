package com.example.aria;

public class DayListItem {
    String time;
    String title;
    String description;
    String id;
    String end;
    String alertString;
    int requestCode;


    public DayListItem(String id, String time, String end, String title, String description, String alertString,int requestCode) {
        this.time = time;
        this.title = title;
        this.description = description;
        this.id = id;
        this.end = end;
        this.alertString = alertString;
        this.requestCode=requestCode;
    }

    public String getTime() {
        return time;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

    public String getEnd() {
        return end;
    }

    public String getAlerts() {
        return alertString;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }
}
