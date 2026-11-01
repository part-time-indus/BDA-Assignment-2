package com.example.hsp_mgmt.model;

import java.math.BigDecimal;


import com.example.hsp_mgmt.model.enums.ItemType;

import jakarta.persistence.CheckConstraint;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// CREATE TABLE bill_item (
//     bill_item_id INT AUTO_INCREMENT PRIMARY KEY,
//     bill_id INT NOT NULL,
//     item_type ENUM('Consultation','Medicine','Lab Test','Room Charge','Procedure') NOT NULL,
//     description VARCHAR(120) NOT NULL,
//     quantity INT NOT NULL DEFAULT 1,
//     unit_price DECIMAL(10,2) NOT NULL,
//     amount DECIMAL(12,2) GENERATED ALWAYS AS (quantity * unit_price) STORED,
//     CONSTRAINT fk_bill_item_bill_id FOREIGN KEY (bill_id)
//     REFERENCES bill(bill_id) ON DELETE CASCADE,
//     CONSTRAINT chk_bitem_qty CHECK (quantity > 0),
//     CONSTRAINT chk_bitem_price CHECK (unit_price >= 0)
// ) ENGINE = InnoDB;



@Entity 
@Table(name = "bill_item", options = "Engine=InnoDB",
       check = { 
        @CheckConstraint(name = "chk_bitem_qty", constraint = "quantity > 0"),
        @CheckConstraint(name = "chk_bitem_price", constraint = "unit_price >= 0")
       })

public class BillItem {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bill_item_id")
    private Long billItemId;

    @ManyToOne()
    @JoinColumn(name = "bill_id", 
                foreignKey = @ForeignKey(name = "fk_bill_item_bill_id"), 
                nullable = false)
    @NotNull 
    private Bill bill;

    @Enumerated(EnumType.STRING)
    @Column(name = "item_type", nullable = false)
    @NotNull 
    private ItemType itemType;

    @Column(nullable = false, length = 120)
    @Size(max = 120)
    @NotNull 
    private String description;

    @Column(nullable = false)
    @NotNull 
    private int quantity = 1;

    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    @NotNull 
    private BigDecimal unitPrice;



    //----------------------------------Only Getters------------------------------------



    @Column(updatable = false, insertable = false, precision = 12, scale = 2)
    private BigDecimal amount;



    //----------------------------------Constructor--------------------------------------


    
    public BillItem(@NotNull Bill bill, @NotNull ItemType itemType, @Size(max = 120) @NotNull String description,
            @NotNull int quantity, @NotNull BigDecimal unitPrice) {
        this.bill = bill;
        this.itemType = itemType;
        this.description = description;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }



    public BillItem() {
    }


    //----------------------------------Getters & Setters--------------------------------



    public Long getBillItemId() {
        return billItemId;
    }



    public Bill getBill() {
        return bill;
    }



    public void setBill(Bill bill) {
        this.bill = bill;
    }



    public ItemType getItemType() {
        return itemType;
    }



    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }



    public String getDescription() {
        return description;
    }



    public void setDescription(String description) {
        this.description = description;
    }



    public int getQuantity() {
        return quantity;
    }



    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }



    public BigDecimal getUnitPrice() {
        return unitPrice;
    }



    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }



    public BigDecimal getAmount() {
        return amount;
    }
    


    
}

