package com.example.aria;

public class DayListItem {
    String time;
    String title;
    String description;

    public DayListItem(String time, String title, String description) {
        this.time = time;
        this.title = title;
        this.description = description;
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
}
