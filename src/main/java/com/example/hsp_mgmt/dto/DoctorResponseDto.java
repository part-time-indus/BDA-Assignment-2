package com.example.hsp_mgmt.dto;

import java.math.BigDecimal;
import java.time.LocalDate;


import com.example.hsp_mgmt.model.enums.EmploymentStatus;
import com.example.hsp_mgmt.model.enums.Gender;

public class DoctorResponseDto {

    private Long doctorId;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String specialisation;
    private Long deptId;
    private String deptName;
    private String deptLocation;
    private LocalDate joinDate;
    private BigDecimal consultationFee;
    private EmploymentStatus status;
    private Gender gender;


    public Long getDoctorId() {
        return doctorId;
    }
    
    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
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
    public void setSecialization(String splz) {
        this.specialisation = splz;
    }
    public Long getDeptId() {
        return deptId;
    }
    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getDeptName(){
        return this.deptName;
    }

    public void setDeptName(String deptName){
        this.deptName = deptName;
    }

    public String getDeptLocation(){
        return this.deptLocation;
    }

    public void setDeptLocation(String deptLocation){
        this.deptLocation = deptLocation;
    }

    public LocalDate getJoinDate() {
        return joinDate;
    }
    public void setJoinDate(LocalDate joinDate) {
        this.joinDate = joinDate;
    }
    public BigDecimal getConsultationFee() {
        return consultationFee;
    }
    public void setConsultationFee(BigDecimal consultationFee) {
        this.consultationFee = consultationFee;
    }
    public EmploymentStatus getStatus() {
        return status;
    }
    public void setStatus(EmploymentStatus status) {
        this.status = status;
    }

    public Gender getGender(){
        return this.gender;
    }

    public void setGender(Gender gender){
        this.gender = gender;
    }
    
}

