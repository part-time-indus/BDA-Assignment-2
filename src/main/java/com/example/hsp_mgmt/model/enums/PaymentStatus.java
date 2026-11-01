package com.example.hsp_mgmt.model.enums;

//'Pending','Partial','Paid'
public enum PaymentStatus {
    PENDING("Pending"),
    PARTIAL("Partial"),
    PAID("Paid");

    private final String status;

    private PaymentStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

        
}
