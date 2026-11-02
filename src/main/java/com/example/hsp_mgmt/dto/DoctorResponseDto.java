package com.example.hsp_mgmt.dto;

import java.math.BigDecimal;
import java.time.LocalDate;


import com.example.hsp_mgmt.model.enums.EmploymentStatus;

public class DoctorResponseDto {

    private Long doctorId;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String splz;
    private int deptId;
    private LocalDate joinDate;
    private BigDecimal consultationFee;
    private EmploymentStatus status;
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
    public String getSplz() {
        return splz;
    }
    public void setSplz(String splz) {
        this.splz = splz;
    }
    public int getDeptId() {
        return deptId;
    }
    public void setDeptId(int deptId) {
        this.deptId = deptId;
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

    
}

