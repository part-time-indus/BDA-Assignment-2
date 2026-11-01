package com.example.hsp_mgmt.model.enums;

public enum BloodGroup {
    APOS("A+"),
    ANEG("A-"),
    BPOS("B+"),
    BNEG("B-"),
    ABPOS("AB+"),
    ABNEG("AB-"),
    OPOS("O+"),
    ONEG("O-");
    
    private final String bGroup;

    
    private BloodGroup(String bGroup) {
        this.bGroup = bGroup;
    }


    public String getbGroup() {
        return bGroup;
    }
    

}
