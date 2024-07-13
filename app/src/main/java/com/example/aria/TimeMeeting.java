package com.example.aria;

public class TimeMeeting {
    private String dateMeeting;
    private String timeMeeting;
    private String timeMeeting2;

    public TimeMeeting(String dateMeeting, String timeMeeting,String timeMeeting2) {
        this.timeMeeting = timeMeeting;
        this.dateMeeting=dateMeeting;
        this.timeMeeting2=timeMeeting2;
    }

    public String getTimeMeeting2() {
        return timeMeeting2;
    }

    public void setTimeMeeting2(String timeMeeting2) {
        this.timeMeeting2 = timeMeeting2;
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
        return this.dateMeeting==obj1.dateMeeting&&this.timeMeeting==obj1.timeMeeting&&this.timeMeeting2==obj1.timeMeeting2;
    }
}
