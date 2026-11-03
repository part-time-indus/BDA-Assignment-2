package com.example.hsp_mgmt.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.example.hsp_mgmt.model.enums.EmploymentStatus;
import com.example.hsp_mgmt.model.enums.Gender;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class DoctorRequestDto {

    @NotBlank(message = "First name is required")
    @Size(max = 40, message = "First name should not exceed 40 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 40, message = "Last name should not exceed 40 characers")
    private String lastName;

    @NotNull(message = "Gender is required")
    private Gender gender;
    
    @NotBlank(message = "Phone number is required")
    @Size(max = 15, message = "Phone number must not exceed 15 characters" )
    private String phone;

    @Email(message = "Invalid email format")
    @Size(max = 80, message = "Email must not exceed 80 characters")
    private String email;

    @NotBlank(message = "Specialisation is required")
    @Size(max = 80, message = "Specialisation should not exceed 80 characters")
    private String splz;

    @NotNull(message = "Department ID is required")
    private Long departmentId;

    @NotNull(message = "Join Date is required")
    private LocalDate joinDate;

    @NotNull(message = "Consultation fee is required")
    private BigDecimal consultationFee = new BigDecimal(500.00);

    @NotNull(message = "Status is required")
    private EmploymentStatus status = EmploymentStatus.ACTIVE;
    
    public DoctorRequestDto() {
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


    public String getSplz() {
        return splz;
    }


    public void setSplz(String splz) {
        this.splz = splz;
    }


    public Long getDepartmentId() {
        return departmentId;
    }


    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }


    public LocalDate getJoinDate() {
        return joinDate;
    }


    public void setJoinDate(LocalDate joinDate) {
        this.joinDate = joinDate;
    }

    public BigDecimal getConsultationFee(){
        return this.consultationFee;
    }

    public void setConsultationFee(BigDecimal fee){
        this.consultationFee = fee;
    }

    public EmploymentStatus getStatus(){
        return this.status;
    }

    public void setStatu(EmploymentStatus status){
        this.status = status;
    }


    

    

}
