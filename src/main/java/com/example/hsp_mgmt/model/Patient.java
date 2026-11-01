package com.example.hsp_mgmt.model;


import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.example.hsp_mgmt.model.enums.*;


// CREATE TABLE patient (
//     patient_id INT AUTO_INCREMENT PRIMARY KEY,
//     first_name VARCHAR(40) NOT NULL,
//     last_name VARCHAR(40) NOT NULL,
//     gender ENUM('Male','Female','Other') NOT NULL,
//     date_of_birth DATE NOT NULL,
//     blood_group ENUM('A+','A-','B+','B-','AB+','AB-','O+','O-') DEFAULT NULL,
//     phone VARCHAR(15) NOT NULL UNIQUE,
//     email VARCHAR(80) DEFAULT NULL UNIQUE,
//     address VARCHAR(150) DEFAULT NULL,
//     city VARCHAR(50) NOT NULL,
//     registered_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
// ) ENGINE = InnoDB;


@Entity 
@Table(name = "patient", options = "Engine=InnoDB")
public class Patient {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patient_id")
    private Long patientId;

    @Column(name = "first_name", nullable = false, length = 40)
    @Size(max = 40)
    @NotNull 
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 40)
    @Size(max = 40)
    @NotNull 
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull 
    private Gender gender;

    @Column(name = "date_of_birth", nullable = false)
    @NotNull 
    private LocalDate dateOfBirth;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "blood_group")
    private BloodGroup bGroup;

    @Column(nullable = false, unique = true, length = 15)
    @Size(max = 15)
    @NotNull 
    private String phone;

    @Column(unique = true, length = 80)
    @Size(max = 80)
    private String email;

    @Column(length = 150)
    @Size(max = 150)
    private String address;

    @Column(nullable = false, length = 50)
    @Size(max = 50)
    @NotNull 
    private String city;



    //----------------------------------Only Getters------------------------------------


    @Column(name = "registered_at", nullable = false, updatable = false)
    @CreationTimestamp 
    @NotNull 
    private LocalDateTime registeredAt;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<Appointment> appointments = new ArrayList<>();

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<Bill> bills = new ArrayList<>();

    
    
    
    //----------------------------------Constructor--------------------------------------

    
    public Patient(@Size(max = 40) @NotNull String firstName, @Size(max = 40) @NotNull String lastName,
            @NotNull Gender gender, @NotNull LocalDate dateOfBirth, BloodGroup bGroup,
            @Size(max = 15) @NotNull String phone, @Size(max = 80) String email, @Size(max = 150) String address,
            @Size(max = 50) @NotNull String city) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.bGroup = bGroup;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.city = city;
    }




    public Patient() {
    }



    //----------------------------------Getters & Setters--------------------------------



    public Long getPatientId() {
        return patientId;
    }



    public String getFirstName() {
        return firstName;
    }




    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }




    public String getLastName() {
        return lastName;
    }




    public void setLastName(String lastName) {
        this.lastName = lastName;
    }




    public Gender getGender() {
        return gender;
    }




    public void setGender(Gender gender) {
        this.gender = gender;
    }




    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }




    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }




    public BloodGroup getbGroup() {
        return bGroup;
    }




    public void setbGroup(BloodGroup bGroup) {
        this.bGroup = bGroup;
    }




    public String getPhone() {
        return phone;
    }




    public void setPhone(String phone) {
        this.phone = phone;
    }




    public String getEmail() {
        return email;
    }




    public void setEmail(String email) {
        this.email = email;
    }




    public String getAddress() {
        return address;
    }




    public void setAddress(String address) {
        this.address = address;
    }




    public String getCity() {
        return city;
    }




    public void setCity(String city) {
        this.city = city;
    }




    public LocalDateTime getRegisteredAt() {
        return registeredAt;
    }





    public List<Appointment> getAppointments() {
        return appointments;
    }


    public List<Bill> getBills() {
        return bills;
    }




    
    
}

