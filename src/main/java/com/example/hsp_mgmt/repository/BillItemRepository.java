package com.example.hsp_mgmt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.hsp_mgmt.model.BillItem;

public interface BillItemRepository extends JpaRepository<BillItem, Long>{

    @Query("SELECT i FROM BillItem i WHERE i.bill.billId = :billId")
    List<BillItem> findByBillId(@Param("billId") Long billId);
    
}
