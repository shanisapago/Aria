package com.example.aria.RetroFitClasses;

import java.util.List;

public class UpdateAriaTimes {
    private List<String> time;
    private List<Integer> day;
    private List<Integer> flag;
    String token;

    public UpdateAriaTimes(List<String> time, List<Integer> day, List<Integer> flag, String token) {
        this.time = time;
        this.day = day;
        this.flag = flag;
        this.token = token;
    }

    public List<String> getTime() {
        return time;
    }

    public void setTime(List<String> time) {
        this.time = time;
    }

    public List<Integer> getDay() {
        return day;
    }

    public void setDay(List<Integer> day) {
        this.day = day;
    }

    public List<Integer> getFlag() {
        return flag;
    }

    public void setFlag(List<Integer> flag) {
        this.flag = flag;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
