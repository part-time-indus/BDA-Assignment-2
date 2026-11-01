package com.example.hsp_mgmt.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.hsp_mgmt.model.Doctor;
import com.example.hsp_mgmt.model.enums.EmploymentStatus;



public interface DoctorRepository extends JpaRepository<Doctor, Long>{

    @Query("SELECT d FROM Doctor d WHERE d.department.deptId = :id")
    List<Doctor> findByDeptId(@Param("id") Long id);

    @Query("SELECT d FROM Doctor d WHERE d.status = :status")
    List<Doctor> findByStatus(@Param("status") EmploymentStatus status);

    @Query("SELECT d FROM Doctor d WHERE d.specialisation = :splz")
    List<Doctor> findBySpecialisation(@Param("splz") String splz);
    
}
