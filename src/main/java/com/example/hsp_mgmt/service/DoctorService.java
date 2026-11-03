package com.example.hsp_mgmt.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import com.example.hsp_mgmt.dto.DoctorRequestDto;
import com.example.hsp_mgmt.dto.DoctorResponseDto;
import com.example.hsp_mgmt.model.enums.DepartmentType;
import com.example.hsp_mgmt.model.enums.EmploymentStatus;


@Service 
public interface DoctorService {

    //CRUD operations
    DoctorResponseDto addDoctor(DoctorRequestDto request);

    DoctorResponseDto updateDoctor(Long doctorId, DoctorRequestDto request);

    DoctorResponseDto getDoctorById(Long doctorId);

    List<DoctorResponseDto> getAllDoctors();

    void deleteDoctor(Long doctorId);


    //Search opeartions

    DoctorResponseDto getDoctorByPhone(String phone);

    DoctorResponseDto getDoctorByEmail(String email);


    List<DoctorResponseDto> getDoctorsBySpecialisation(String specialisation);


    List<DoctorResponseDto> getDoctorsByDepartment(Long deptId);

    List<DoctorResponseDto> getDoctorsByDepartmentName(DepartmentType dept);

    List<DoctorResponseDto> getDoctorsByDepartmentAndStatus(Long deptId, EmploymentStatus status);


    //Update operations

    DoctorResponseDto updateDoctorStatus(Long doctorId, EmploymentStatus status);

    DoctorResponseDto updateConsultationFee(Long doctorId, BigDecimal fee);

    DoctorResponseDto changeDoctorDepartment(Long doctorId, Long newDeptId);


}
