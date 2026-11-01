package com.example.hsp_mgmt.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.hsp_mgmt.model.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    @Query("SELECT p FROM Patient p WHERE p.phone = :phone")
    Optional<Patient> findByPhone(@Param("phone") String phone);

    @Query("SELECT p FROM Patient p WHERE p.email = :email")
    Optional<Patient> findByEmail(@Param("email") String email);

    @Query("SELECT p FROM Patient p WHERE p.lastName = :lastName")
    List<Patient> findByLastName(@Param("lastName") String lastName);

   
}
