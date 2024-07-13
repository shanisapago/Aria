package com.example.aria;

public class OpenEventsListItem {
    String Title;
    String Description;

    public OpenEventsListItem(String title, String description) {
        Title = title;
        Description = description;
    }

    public String getTitle() {
        return Title;
    }

    public String getDescription() {
        return Description;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setDescription(String description) {
        Description = description;
    }
}