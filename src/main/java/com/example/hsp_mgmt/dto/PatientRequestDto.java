package com.example.hsp_mgmt.dto;

import java.time.LocalDate;

import com.example.hsp_mgmt.model.enums.BloodGroup;
import com.example.hsp_mgmt.model.enums.Gender;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

public class PatientRequestDto {

    @NotBlank (message = "First name is required")
    @Size(max = 40, message = "First name must not exceed 40 characters")
    private String firstName;

    @NotBlank (message = "Last name is required")
    @Size(max = 40, message = "Last name must not exceed 40 characters")
    private String lastName;

    @NotNull(message = "Gender is required")
    private Gender gender;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    private BloodGroup bGroup;

    @NotBlank(message = "Phone number is required")
    @Size(max = 15, message = "Phone number must not exceed 15 characters" )
    private String phone;

    @Email(message = "Invalid email format")
    @Size(max = 80, message = "Email must not exceed 80 characters")
    private String email;
    
    @Size(max = 150, message = "Address must not exceed 150 characters")
    private String address;

    @NotBlank(message = "City is required")
    @Size(max = 50, message = "City must not exceed 50 chatacters")
    private String city;



    public PatientRequestDto() {
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

    
    

    
}
