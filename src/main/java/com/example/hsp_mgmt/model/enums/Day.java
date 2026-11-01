package com.example.hsp_mgmt.model.enums;

public enum Day {
    MON("Mon"),
    TUE("Tue"),
    WED("Wed"),
    THU("Thu"),
    FRI("Fri"),
    SAT("Sat"),
    SUN("Sun");

    private final String day;
    private Day(String day) {
        this.day = day;
    }
    public String getDay() {
        return day;
    }
       
    
}

