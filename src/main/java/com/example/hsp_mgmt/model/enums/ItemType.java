package com.example.hsp_mgmt.model.enums;

//'Consultation','Medicine','Lab Test','Room Charge','Procedure')
public enum ItemType {
    CONSULTATION("Consultation"),
    MEDICINE("Medicine"),
    LAB_TEST("Lab Test"),
    ROOM_CHARGE("Room Charge"),
    PROCEDURE("Procedure");

    private final String item;

    private ItemType(String item) {
        this.item = item;
    }

    public String getItem() {
        return item;
    }

    
    
}
