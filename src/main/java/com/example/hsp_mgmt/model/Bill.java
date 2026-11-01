package com.example.hsp_mgmt.model;

import java.util.ArrayList;
import java.util.List;

import com.example.hsp_mgmt.model.enums.PaymentStatus;


import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;



// CREATE TABLE bill (
//     bill_id INT AUTO_INCREMENT PRIMARY KEY,
//     patient_id INT NOT NULL,
//     appointment_id INT DEFAULT NULL UNIQUE,
//     admission_id INT DEFAULT NULL UNIQUE,
//     bill_date DATE NOT NULL,
//     total_amount DECIMAL(12,2) NOT NULL DEFAULT 0.00,
//     insurance_covered DECIMAL(12,2) NOT NULL DEFAULT 0.00,
//     payment_status ENUM('Pending','Partial','Paid') NOT NULL DEFAULT 'Pending',
//     CONSTRAINT fk_bill_patient_id FOREIGN KEY (patient_id)
//     REFERENCES patient(patient_id),
//     CONSTRAINT fk_bill_appointment_id FOREIGN KEY (appointment_id)
//     REFERENCES appointment(appointment_id),
//     CONSTRAINT fk_bill_admission_id FOREIGN KEY (admission_id)
//     REFERENCES admission(admission_id),
//     CONSTRAINT chk_bill_amounts CHECK (total_amount >= 0 AND insurance_covered >= 0
//     AND insurance_covered <= total_amount),
//     CONSTRAINT chk_bill_source CHECK (appointment_id IS NOT NULL OR admission_id IS NOT NULL)
// ) ENGINE = InnoDB;




@Entity 
@Table(name = "bill", options = "Engine=InnoDB",
       check = {
        @CheckConstraint(name = "chk_bill_amounts", constraint = "total_amount >= 0"),
        @CheckConstraint(name = "chk_bill_source", constraint = "appointment_id IS NOT NULL")
       }
)
public class Bill {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bill_id")
    private Long billId;

    @ManyToOne 
    @JoinColumn(name = "patient_id",
                foreignKey = @ForeignKey(name = "fk_bill_patient_id"),
                nullable = false)
    @NotNull 
    private Patient patient;
    
    @OneToOne 
    @JoinColumn(name = "appointment_id",
                foreignKey = @ForeignKey(name = "fk_bill_appointment_id"),
                unique = true)
    private Appointment appointment;

    //private Admission admission;
    
    @Column(name = "bill_date", nullable = false)
    @NotNull 
    private LocalDate billDate;
    
    @Column(name = "total_amount", nullable = false, scale = 2, precision = 12)
    @NotNull 
    private BigDecimal totalAmount = new BigDecimal(0.00);
    
    //private BigDecimal insuranceCovered

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    @NotNull 
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;



    //----------------------------------Only Getters------------------------------------

    
    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL)
    private List<BillItem> billItems = new ArrayList<>();

    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL)
    private List<Payment> payments = new ArrayList<>();



    //----------------------------------Constructor--------------------------------------



    public Bill(@NotNull Patient patient, Appointment appointment, @NotNull LocalDate billDate,
            @NotNull BigDecimal totalAmount, @NotNull PaymentStatus paymentStatus) {
        this.patient = patient;
        this.appointment = appointment;
        this.billDate = billDate;
        this.totalAmount = totalAmount;
        this.paymentStatus = paymentStatus;
    }






    public Bill() {
    }



    //----------------------------------Getters & Setters--------------------------------



    public Long getBillId() {
        return billId;
    }



    public Patient getPatient() {
        return patient;
    }






    public void setPatient(Patient patient) {
        this.patient = patient;
    }






    public Appointment getAppointment() {
        return appointment;
    }






    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }






    public LocalDate getBillDate() {
        return billDate;
    }






    public void setBillDate(LocalDate billDate) {
        this.billDate = billDate;
    }






    public BigDecimal getTotalAmount() {
        return totalAmount;
    }






    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }






    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }






    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }






    public List<BillItem> getBillItems() {
        return billItems;
    }




    public List<Payment> getPayments() {
        return payments;
    }

  
}

