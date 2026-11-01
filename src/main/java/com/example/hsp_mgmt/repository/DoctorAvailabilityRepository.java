package com.example.hsp_mgmt.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.hsp_mgmt.model.DoctorAvailability;
import com.example.hsp_mgmt.model.enums.Day;

public interface DoctorAvailabilityRepository extends JpaRepository<DoctorAvailability, Long> {

    @Query("SELECT da FROM DoctorAvailability da WHERE da.doctor.doctorId = :doctorId")
    List<DoctorAvailability> findByDoctorId(@Param("doctorId") Long doctorId);
    
    @Query("SELECT da FROM DoctorAvailability da WHERE da.doctor.doctorId = :doctorId AND da.dayOfWeek = :day")
    Optional<DoctorAvailability> findByDoctorIdAndDay(@Param("doctorId") Long doctorId, @Param("day") Day day);

    
}
