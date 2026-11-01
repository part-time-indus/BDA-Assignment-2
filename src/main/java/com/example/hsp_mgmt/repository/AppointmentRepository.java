package com.example.hsp_mgmt.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.hsp_mgmt.model.Appointment;
import com.example.hsp_mgmt.model.enums.AppointmentStatus;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("SELECT a FROM Appointment a WHERE a.doctor.doctorId = :doctorId")
    List<Appointment> findByDoctorId(@Param("doctorId") Long doctorId);
    @Query("SELECT a FROM Appointment a WHERE a.doctor.doctorId = :doctorId AND a.appointmentDate = :date")
    List<Appointment> findByDoctorIdAndApptDate(@Param("doctorId") Long doctorId, @Param("date") LocalDate date);

    @Query("SELECT a FROM Appointment a WHERE a.patient.patientId = :patientId")
    List<Appointment> findByPatientId(@Param("patientId") Long patientId);

    @Query("SELECT a FROM Appointment a WHERE a.activeSlot = :activeSlot")
    Optional<Appointment> findByActiveSlot(@Param("activeSlot") String activeSlot);

    @Query("SELECT a FROM Appointment a WHERE a.apptStatus = :status")
    List<Appointment> findByApptStatus(@Param("status") AppointmentStatus status);

    
}
