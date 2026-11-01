package com.example.hsp_mgmt.model.enums;

public enum EmploymentStatus {
    ACTIVE("Active"),
    ONLEAVE("OnLeave"),
    RESIGNED("Resigned");

    private final String status;

 
    private EmploymentStatus(String status) {
        this.status = status;
    }


    public String getStatus() {
        return status;
    }
    
    
}
