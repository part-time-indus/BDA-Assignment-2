package com.example.hsp_mgmt.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.hsp_mgmt.model.Bill;
import com.example.hsp_mgmt.model.enums.PaymentStatus;

public interface BillRepository extends JpaRepository<Bill, Long>{
    @Query("SELECT b FROM Bill b WHERE b.patient.patientId = :patientId")
    List<Bill> findByPatientId(@Param("patientId") Long patientId);
    
    @Query("SELECT b FROM Bill b WHERE b.appointment.appointmentId = :apptId")
    Optional<Bill> findByAppointmentId(@Param("apptId") Long apptId);

    @Query("SELECT b FROM Bill b WHERE b.paymentStatus = :status")
    List<Bill> findByPaymentStatus(@Param("status") PaymentStatus status);
    
}
