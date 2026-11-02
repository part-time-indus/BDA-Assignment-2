package com.example.hsp_mgmt.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class DepartmentRequestDto {

    @NotBlank(message = "Department name is required")
    @Size(max = 60, message = "Department name cannot exceed 60 characters")
    private String deptName;

    @NotBlank(message = "Department location is required")
    @Size(max = 60, message = "Location cannot exceed 60 characters")
    private String location;

    @Size(max = 15, message = "Phone number cannot exceed 15 characters")
    private String phone;

    // Getters and Setters

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
