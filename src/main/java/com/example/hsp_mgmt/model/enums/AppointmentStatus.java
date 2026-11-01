package com.example.hsp_mgmt.model.enums;

public enum AppointmentStatus {
    SCHEDULED("Scheduled"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled"),
    NOSHOW("No-Show");

    private final String apptStatus;

    private AppointmentStatus(String apptStatus) {
        this.apptStatus = apptStatus;
    }

    public String getApptStatus() {
        return apptStatus;
    }

    
}
