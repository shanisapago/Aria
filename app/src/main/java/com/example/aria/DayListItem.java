package com.example.aria;

public class DayListItem {
    String time;
    String title;
    String description;
    String id;
    String end;
    String alertString;


    public DayListItem(String id, String time, String end, String title, String description, String alertString) {
        this.time = time;
        this.title = title;
        this.description = description;
        this.id = id;
        this.end = end;
        this.alertString = alertString;
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
}
