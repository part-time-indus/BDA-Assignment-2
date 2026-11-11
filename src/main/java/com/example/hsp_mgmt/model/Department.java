package com.example.hsp_mgmt.model;

import java.util.*;

import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


// CREATE TABLE department (
//     dept_id INT AUTO_INCREMENT PRIMARY KEY,
//     dept_name VARCHAR(60) NOT NULL UNIQUE,
//     location VARCHAR(60) NOT NULL,
//     phone VARCHAR(15) DEFAULT NULL
// ) ENGINE = InnoDB;


@Entity
@Table(name = "department")
public class Department {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dept_id")
    private Long deptId;

    @Column(name = "dept_name", length = 60, nullable = false, unique = true)
    @NotNull
    private String deptName;
    
    @Column(length = 60, nullable = false)
    @Size(max = 60)
    private String location;
    
    @Column(length = 15)
    @Size(max = 15)
    private String phone;



    
    //----------------------------------Only Getters------------------------------------



    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL)
    private List<Doctor> doctors = new ArrayList<>();


    
    //----------------------------------Constructor--------------------------------------


    public Department(@NotNull String deptName, @Size(max = 60) String location, @Size(max = 15) String phone) {
        this.deptName = deptName;
        this.location = location;
        this.phone = phone;
    }



    public Department() {
    }



    //----------------------------------Getters & Setters--------------------------------


    public Long getDeptId() {
        return deptId;
    }



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



    public List<Doctor> getDoctors() {
        return doctors;
    }


    
}

