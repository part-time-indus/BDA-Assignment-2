package com.example.hsp_mgmt.model.enums;


//'Cash','Card','UPI','Net Banking'

public enum PaymentMode {
    CASH("Cash"),
    CARD("Card"),
    UPI("UPI"),
    NET_BANKING("Net Banking");


    private final String mode;

    private PaymentMode(String mode) {
        this.mode = mode;
    }

    public String getMode() {
        return mode;
    }

    
    
}
