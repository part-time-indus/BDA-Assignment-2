package com.example.hsp_mgmt.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;


import com.example.hsp_mgmt.model.enums.PaymentMode;

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

// CREATE TABLE payment (
//     payment_id INT AUTO_INCREMENT PRIMARY KEY,
//     bill_id INT NOT NULL,
//     payment_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
//     amount DECIMAL(12,2) NOT NULL,
//     payment_mode ENUM('Cash','Card','UPI','Net Banking') NOT NULL,
//     reference_no VARCHAR(40) DEFAULT NULL UNIQUE,
//     CONSTRAINT fk_payment_bill_id FOREIGN KEY (bill_id)
//     REFERENCES bill(bill_id),
//     CONSTRAINT chk_pay_amount CHECK (amount > 0)
// ) ENGINE = InnoDB;


@Entity 
@Table(name = "payment", options = "Engine=InnoDB",
       check = @CheckConstraint(name = "chk_pay_amount", constraint = "amount > 0") 
)
public class Payment {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long paymentId;

    @ManyToOne 
    @JoinColumn(name = "bill_id", 
                foreignKey = @ForeignKey(name = "fk_payment_bill_id"),
                nullable = false)
    @NotNull 
    private Bill bill;


    @Column(nullable = false, precision = 12, scale = 2)
    @NotNull 
    private BigDecimal amount;

    @Column(name = "payment_mode", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull 
    private PaymentMode paymentMode;

    @Column(name = "reference_no", unique = true, length = 40)
    @Size(max = 40)
    private String referenceNo;


    //----------------------------------Only Getters------------------------------------


    @Column(name = "payment_date", nullable = false, updatable = false)
    @CreationTimestamp 
    @NotNull 
    private LocalDateTime paymentDate;



    //----------------------------------Constructor--------------------------------------



    public Payment(@NotNull Bill bill, @NotNull BigDecimal amount, @NotNull PaymentMode paymentMode,
            @Size(max = 40) String referenceNo) {
        this.bill = bill;
        this.amount = amount;
        this.paymentMode = paymentMode;
        this.referenceNo = referenceNo;
    }


    public Payment() {
    }


    //----------------------------------Getters & Setters--------------------------------


    public Long getPaymentId() {
        return paymentId;
    }



    public Bill getBill() {
        return bill;
    }


    public void setBill(Bill bill) {
        this.bill = bill;
    }


    public BigDecimal getAmount() {
        return amount;
    }


    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }


    public PaymentMode getPaymentMode() {
        return paymentMode;
    }


    public void setPaymentMode(PaymentMode paymentMode) {
        this.paymentMode = paymentMode;
    }


    public String getReferenceNo() {
        return referenceNo;
    }


    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }


    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }


    
}
