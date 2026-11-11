package com.example.hsp_mgmt.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.hsp_mgmt.model.Doctor;
import com.example.hsp_mgmt.model.enums.EmploymentStatus;



public interface DoctorRepository extends JpaRepository<Doctor, Long>{



// -----------FIND QUERIES ----------------------------------

// Look up doctor by phone

Optional<Doctor> findByPhone(String phone);

// Look up doctor by email

Optional<Doctor> findByEmailIgnoreCase(String email);

// Look up doctor by specialization

List<Doctor> findBySpecialisationIgnoreCase(String specialisation);

// Get list of doctors from a particular department using department Id

@Query("SELECT d FROM Doctor d WHERE d.department.deptId = :deptId")
List<Doctor> findDeptId(@Param("deptId") Long deptId);

// Get list of doctors along with their department information

@Query("SELECT d FROM Doctor d JOIN FETCH d.department")
List<Doctor> findAllDoctorsWithDepartment();

// Get list of doctors from a particular department using department name

@Query("SELECT d FROM Doctor d WHERE d.department.deptName = :deptName")
List<Doctor> findDoctorsByDepartmentName(@Param("deptName")String deptName);



//------------UPDATE QUERIES---------------------------------

// change doctor phone

@Modifying(clearAutomatically = true, flushAutomatically = true)
@Query("Update Doctor d SET d.phone = :phone WHERE d.doctorId = :docId")
int updateDoctorPhone(@Param("doctorId") Long docId, @Param("phone") String phone);

// change doctor status

@Modifying(clearAutomatically = true, flushAutomatically = true)
@Query("Update Doctor d SET d.status = :status WHERE d.doctorId = :docId")
int updateDoctorStatus(@Param("doctorId") Long docId, @Param("status")EmploymentStatus status);

// change doctor fee

@Modifying(clearAutomatically = true, flushAutomatically = true)
@Query("Update Doctor d SET d.consultationFee = :fee WHERE d.doctorId = :docId")
int updateDoctorStatus(@Param("doctorId") Long docId, @Param("fee") BigDecimal fee);



    
}
