package com.example.aria;

public class DayListItem {
    String time;
    String title;
    String description;
    String id;
    String end;
    Alert alert;

    public DayListItem(String time, String title, String description,String id,String end,Alert  alert) {
        this.time = time;
        this.title = title;
        this.description = description;
        this.id=id;
        this.end=end;
        this.alert=alert;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public Alert getAlert() {
        return alert;
    }

    public void setAlert(Alert alert) {
        this.alert = alert;
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
