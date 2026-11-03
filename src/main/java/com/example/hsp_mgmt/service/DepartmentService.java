package com.example.hsp_mgmt.service;

import java.util.List;

import com.example.hsp_mgmt.dto.DepartmentRequestDto;
import com.example.hsp_mgmt.dto.DepartmentResponseDto;
import com.example.hsp_mgmt.model.enums.DepartmentType;
import com.example.hsp_mgmt.model.enums.EmploymentStatus;

public interface DepartmentService {

    // CRUD operations
    DepartmentResponseDto createDepartment(DepartmentRequestDto request);

    DepartmentResponseDto updateDepartment(Long deptId, DepartmentRequestDto request);

    DepartmentResponseDto getDepartmentById(Long deptId);

    List<DepartmentResponseDto> getAllDepartments();

    void deleteDepartment(Long deptId);


    // Search operations
    DepartmentResponseDto getDepartmentByName(DepartmentType dept);

    List<DepartmentResponseDto> getDepartmentsByLocation(String location);


    // Department and doctor operations

    List<DepartmentResponseDto> getDepartmentsWithDoctorsByStatus(EmploymentStatus status);

    List<DepartmentResponseDto> getDepartmentsWithoutDoctors();

    List<DepartmentResponseDto> getDepartmentsBySpecialisation(String specialisation);


    // Update operations
    DepartmentResponseDto updateDepartmentLocation(Long deptId, String location);

    DepartmentResponseDto updateDepartmentPhone(Long deptId, String phone);
}
