package com.example.hsp_mgmt.config;

import java.util.List;

import com.example.hsp_mgmt.model.Department;



public class DepartmentData{
    public static List<Department> get(){
        return List.of(
            new Department("Cardiology", "Block A, Floor 2", "0112000101" ),
            new Department("Orthopedics","Block B, Floor 1", "0112000102"),
            new Department("Pediatrics","Block A, Floor 1", "0112000103"),
            new Department("General Medicine", "Block C, Floor 1", "0112000104"),
            new Department("Neurology","Block A, Floor 3", "0112000105"),
            new Department("Pathology","Block D, Ground", "0112000106"),
            new Department("Oncology", "Block C, Floor 1", "0112000107"),
            new Department("Dermatoloy", "Block D, Floor 2", "0112000108"),
            new Department("Radiology", "Block D, Floor 2", "0112000109"),
            new Department("Urology", "Block D, Floor 2", "0112000111")
        );
    }


}