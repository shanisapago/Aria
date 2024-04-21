package com.example.aria.RetroFitClasses;

import java.util.ArrayList;

public class PhoneArray {
    private ArrayList<String> phones;
    public PhoneArray(ArrayList<String> phones){
        this.phones = phones;
    }
    public void addPhone(String number){
        this.phones.add(number);
    }

    public ArrayList<String> getPhones() {
        return phones;
    }

    public void setPhones(ArrayList<String> phones) {
        this.phones = phones;
    }
}
