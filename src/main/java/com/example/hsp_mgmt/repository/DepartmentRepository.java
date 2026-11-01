package com.example.hsp_mgmt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.hsp_mgmt.model.Department;
import java.util.Optional;


public interface DepartmentRepository extends JpaRepository<Department,Long>{
    
    @Query("SELECT d FROM Department d WHERE d.deptName = :deptName")
    Optional<Department> findByDeptName(@Param("deptName") String deptName);
    
}
