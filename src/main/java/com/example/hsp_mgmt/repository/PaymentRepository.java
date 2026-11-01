package com.example.hsp_mgmt.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.hsp_mgmt.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long>{

    @Query("SELECT p FROM Payment p WHERE p.bill.billId = :billId")
    List<Payment> findByBillId(@Param("billId") Long billId);

    @Query("SELECT p FROM Payment p WHERE p.referenceNo = :refNo")
    Optional<Payment> findByRefNo(@Param("refNo") String refNo);
    
}
