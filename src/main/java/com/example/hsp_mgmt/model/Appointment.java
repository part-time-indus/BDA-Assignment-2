package com.example.hsp_mgmt.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.hibernate.annotations.CreationTimestamp;

import com.example.hsp_mgmt.model.enums.AppointmentStatus;

import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// CREATE TABLE appointment (
//     appointment_id INT AUTO_INCREMENT PRIMARY KEY,
//     patient_id INT NOT NULL,
//     doctor_id INT NOT NULL,
//     appointment_date DATE NOT NULL,
//     appointment_time TIME NOT NULL,
//     reason VARCHAR(200) DEFAULT NULL,
//     status ENUM('Scheduled','Completed','Cancelled','No-Show') NOT NULL DEFAULT 'Scheduled',
//     created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
//     active_slot VARCHAR(40) GENERATED ALWAYS AS (IF(status IN ('Scheduled','Completed'),
//     CONCAT(doctor_id,'#',appointment_date,'#',appointment_time), NULL)) STORED,
//     CONSTRAINT fk_appointment_patient_id FOREIGN KEY (patient_id)
//     REFERENCES patient(patient_id),
//     CONSTRAINT fk_appointment_doctor_id FOREIGN KEY (doctor_id)
//     REFERENCES doctor(doctor_id),
//     CONSTRAINT uq_active_slot UNIQUE (active_slot)
// ) ENGINE = InnoDB;


@Entity 
@Table(name = "appointment", options = "Engine=InnoDB", 
       uniqueConstraints = @UniqueConstraint(name = "uq_active_slot", columnNames = {"active_slot"}))

public class Appointment {
    
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointment_id")
    private Long appointmentId;

    @ManyToOne 
    @JoinColumn(name = "patient_id", 
                foreignKey = @ForeignKey(name = "fk_appointment_patient_id"), 
                nullable = false)
    @NotNull 
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id", 
                foreignKey = @ForeignKey(name = "fk_appointment_doctor_id"), 
                nullable = false)
    @NotNull 
    private Doctor doctor;

    @Column(name = "appointment_date", nullable = false)
    @NotNull 
    private LocalDate appointmentDate;

    @Column(name = "appointment_time", nullable = false)
    @NotNull 
    private LocalTime appointmentTime;

    @Column(length = 200)
    @Size(max = 200)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @NotNull 
    private AppointmentStatus apptStatus = AppointmentStatus.SCHEDULED;


    //----------------------------------Only Getters------------------------------------


    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp 
    @NotNull
    private LocalDateTime createdAt;

    @Column(name = "active_slot", insertable = false, updatable = false)
    private String activeSlot;
    
    @OneToOne(mappedBy = "appointment", cascade = CascadeType.ALL) 
    private Bill bill;



    //----------------------------------Constructor--------------------------------------


    public Appointment(@NotNull Patient patient, @NotNull Doctor doctor, @NotNull LocalDate appointment_date,
            @NotNull LocalTime appointment_time, @Size(max = 200) String reason,
            @NotNull AppointmentStatus apptStatus) {
        this.patient = patient;
        this.doctor = doctor;
        this.appointmentDate = appointment_date;
        this.appointmentTime = appointment_time;
        this.reason = reason;
        this.apptStatus = apptStatus;
    }

    public Appointment() {
    }




    //----------------------------------Getters & Setters--------------------------------


    public Long getAppointmentId() {
        return appointmentId;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public LocalDate getAppointment_date() {
        return appointmentDate;
    }

    public void setAppointment_date(LocalDate appointment_date) {
        this.appointmentDate = appointment_date;
    }

    public LocalTime getAppointment_time() {
        return appointmentTime;
    }

    public void setAppointment_time(LocalTime appointment_time) {
        this.appointmentTime = appointment_time;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public AppointmentStatus getApptStatus() {
        return apptStatus;
    }

    public void setApptStatus(AppointmentStatus apptStatus) {
        this.apptStatus = apptStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }


    public String getActiveSlot() {
        return activeSlot;
    }


    public Bill getBill() {
        return bill;
    }


    

    

}
