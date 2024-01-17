package com.example.aria;

public class TimeMeeting {
    private String dateMeeting; //date
    private String timeMeeting; //hours and minutes

    public TimeMeeting(String dateMeeting, String timeMeeting) {
        this.timeMeeting = timeMeeting;
        this.dateMeeting=dateMeeting;
    }

    public String getDateMeeting() {
        return dateMeeting;
    }

    public String getTimeMeeting() {
        return timeMeeting;
    }

    public void setDateMeeting(String dateMeeting) {
        this.dateMeeting = dateMeeting;
    }

    public void setTimeMeeting(String timeMeeting) {
        this.timeMeeting = timeMeeting;
    }
}
