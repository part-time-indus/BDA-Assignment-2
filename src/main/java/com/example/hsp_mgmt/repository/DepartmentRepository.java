package com.example.hsp_mgmt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import com.example.hsp_mgmt.model.Department;
import com.example.hsp_mgmt.model.enums.EmploymentStatus;

import java.util.List;
import java.util.Optional;


public interface DepartmentRepository extends JpaRepository<Department,Long>{
    
// find department information using its Id

Optional<Department> findByDeptId(String deptId);

// find department information using its name

Optional<Department> findByDeptNameIgnoreCase(String deptName);

// find departments in a particular location
List<Department>findByLocationContainingIgnoreCase(String location);

// list departments with doctors having given status

@Query("SELECT DISTINCT dep FROM Department dep JOIN  dep.doctors doc WHERE doc.status = :status  ")
List<Department> findDepartmentsWithDoctorStatus(@Param("status") EmploymentStatus status);

// list departments with give specialisation

@Query("SELECT DISTINCT dep FROM Department dep JOIN dep.doctors doc WHERE doc.specialisation = :splz")
List<Department> findDepartmentsBySpecialisation(String splz);

// list departments with no doctor

@Query("SELECT dep FROM Department dep WHERE dep.doctors IS NULL")
List<Department> findDepartmentsWithoutDoctors();

// update department location

@Query("UPDATE Department d SET d.location = :loc WHERE d.deptId = :deptId")
int updateDepartmentLocation(@Param("deptId") String deptId, @Param("loc") String loc);

// update department phone

@Query("UPDATE Department d SET d.phone = :phone WHERE d.deptId = :deptId")
int updateDepartmentPhone(@Param("deptId") String deptId, @Param("phone") String phone);










    
}
