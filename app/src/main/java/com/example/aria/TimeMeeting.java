package com.example.aria;

public class TimeMeeting {
    private String dateMeeting;
    private String timeMeeting;

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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        TimeMeeting obj1 = (TimeMeeting) obj;
        return this.dateMeeting==obj1.dateMeeting&&this.timeMeeting==obj1.timeMeeting;
    }
}
