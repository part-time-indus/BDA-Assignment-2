package com.example.hsp_mgmt.model;

import java.util.*;

import com.example.hsp_mgmt.model.enums.Gender;
import com.example.hsp_mgmt.model.enums.EmploymentStatus;
import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


// CREATE TABLE doctor(
//     doctor_id INT AUTO_INCREMENT PRIMARY KEY,
//     first_name VARCHAR(40) NOT NULL,
//     last_name  VARCHAR(40) NOT NULL,
//     gender ENUM('Male', 'Female', 'Other') NOT NULL.
//     phone VARCHAR(15) NOT NULL UNIQUE,
//     email VARCHAR(40) NOT NULL UNIQUE,  
//     specialisation VARCHAR(60) NOT NULL UNIQUE,
//     dept_id INT NOT NULL,
//     consultation_fee DECIMAL(8,2) NOT NULL DEFAULT 500.00,
//     join_date DATE NOT NULL,
//     status ENUM('Active', 'On Leave', 'Resigned') NOT NULL DEFAULT 'Active',
//     CONSTRAINT fk_doctor_dept_id FOREIGN KEY(dept_id) REFERENCES department(dept_id),
//     CONSTRAINT chk_doctor_fee CHECK(consultation_fee >= 0)
// )ENGINE = InnoDB;


@Entity 
@Table(name = "doctor", options = "Engine=InnoDB",
       check = @CheckConstraint(name = "chk_doctor_fee", constraint = "consultation_fee >= 0")
)
public class Doctor {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doctor_id")
    private Long doctorId;

    @Column(name = "first_name", nullable = false, length = 40)
    @NotNull 
    @Size(max=40)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 40)
    @NotNull 
    @Size(max = 40)
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull 
    private Gender gender;

    @Column(nullable = false, unique = true, length = 15)
    @Size(max = 15)
    @NotNull 
    private String phone;

    @Column(unique = true, length = 80)
    @Size(max = 80)
    private String email;

    @Column(nullable = false, unique = true, length = 60)
    @NotNull 
    @Size(max=60)
    private String specialisation;

    @ManyToOne 
    @JoinColumn(name = "dept_id", 
                foreignKey = @ForeignKey(name = "fk_doctor_dept_id"), 
                nullable = false)
    @NotNull 
    private Department department;

    @Column(name = "consultation_fee", nullable = false, precision = 8, scale = 2)
    @NotNull 
    private BigDecimal consultationFee = new BigDecimal(500.0);

    @Column(name = "join_date",nullable = false)
    @NotNull 
    private LocalDate joinDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull 
    private EmploymentStatus status = EmploymentStatus.ACTIVE;



    //----------------------------------Constructor--------------------------------------
    

    public Doctor(@NotNull @Size(max = 40) String firstName, @NotNull @Size(max = 40) String lastName,
            @NotNull Gender gender, @Size(max = 15) @NotNull String phone, @Size(max = 80) String email,
            @NotNull @Size(max = 60) String specialisation, @NotNull Department department,
            @NotNull BigDecimal consultationFee, @NotNull LocalDate joinDate, @NotNull EmploymentStatus status) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.specialisation = specialisation;
        this.department = department;
        this.consultationFee = consultationFee;
        this.joinDate = joinDate;
        this.status = status;
    }


    public Doctor() {
    }


    public Long getDoctorId() {
        return doctorId;
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


    public String getSpecialisation() {
        return specialisation;
    }


    public void setSpecialisation(String specialisation) {
        this.specialisation = specialisation;
    }


    public Department getDepartment() {
        return department;
    }


    public void setDepartment(Department department) {
        this.department = department;
    }


    public BigDecimal getConsultationFee() {
        return consultationFee;
    }


    public void setConsultationFee(BigDecimal consultationFee) {
        this.consultationFee = consultationFee;
    }


    public LocalDate getJoinDate() {
        return joinDate;
    }


    public void setJoinDate(LocalDate joinDate) {
        this.joinDate = joinDate;
    }


    public EmploymentStatus getStatus() {
        return status;
    }


    public void setStatus(EmploymentStatus status) {
        this.status = status;
    }


    //----------------------------------Getters & Setters--------------------------------






}

