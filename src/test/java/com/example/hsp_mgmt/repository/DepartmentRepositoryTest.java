package com.example.hsp_mgmt.repository; 
import com.example.hsp_mgmt.model.Department;
import com.example.hsp_mgmt.model.Doctor;
import com.example.hsp_mgmt.model.enums.EmploymentStatus;
import com.example.hsp_mgmt.model.enums.Gender;

import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
class DepartmentRepositoryTest {

    @Autowired
    private DepartmentRepository deptRepo;

    @Autowired
    private DoctorRepository docRepo;

    @Autowired
    private EntityManager em;

    // ---------- helpers ----------

    private Department saveDept(String name, String location) {
        Department d = new Department();
        d.setDeptName(name);
        d.setLocation(location);
        d.setPhone("0123456789");
        return deptRepo.saveAndFlush(d);
    }

    private Doctor saveDoctor(Department dept, String phone, String specialisation, EmploymentStatus status) {
        Doctor d = new Doctor();
        d.setFirstName("First");
        d.setLastName("Last");
        d.setGender(Gender.values()[0]);
        d.setPhone(phone);
        d.setSpecialisation(specialisation);
        d.setDepartment(dept);
        d.setConsultationFee(new BigDecimal("500.00"));
        d.setJoinDate(LocalDate.of(2020, 1, 1));
        d.setStatus(status);
        return docRepo.saveAndFlush(d);
    }

    private EmploymentStatus otherStatus() {
        return Arrays.stream(EmploymentStatus.values())
                .filter(s -> s != EmploymentStatus.ACTIVE)
                .findFirst()
                .orElseThrow();
    }

    private Set<String> names(List<Department> list) {
        return list.stream().map(Department::getDeptName).collect(Collectors.toSet());
    }

    // ---------- derived queries ----------

    @Test
    void findByDeptNameIgnoreCase_ignoresCase() {
        saveDept("Cardiology", "Block A");
        em.clear();

        Optional<Department> found = deptRepo.findByDeptNameIgnoreCase("cARDIOLOGY");

        assertTrue(found.isPresent());
        assertEquals("Cardiology", found.get().getDeptName());
    }

    @Test
    void findByDeptNameIgnoreCase_unknownName_returnsEmpty() {
        saveDept("Cardiology", "Block A");

        assertTrue(deptRepo.findByDeptNameIgnoreCase("Ghost").isEmpty());
    }

    @Test
    void findByLocationContainingIgnoreCase_matchesPartialText() {
        saveDept("Cardiology", "Block A");
        saveDept("Neurology", "Block B");
        saveDept("Radiology", "Annex");
        em.clear();

        assertEquals(Set.of("Cardiology", "Neurology"), names(deptRepo.findByLocationContainingIgnoreCase("block")));
        assertEquals(Set.of("Radiology"), names(deptRepo.findByLocationContainingIgnoreCase("ANNEX")));
        assertTrue(deptRepo.findByLocationContainingIgnoreCase("nowhere").isEmpty());
    }

    // ---------- @Query methods ----------

    @Test
    void findDepartmentsWithDoctorStatus_returnsEachMatchingDepartmentOnce() {
        Department a = saveDept("Cardiology", "Block A");
        Department b = saveDept("Neurology", "Block B");
        saveDept("Radiology", "Annex"); // no doctors
        saveDoctor(a, "1001", "Cardiologist", EmploymentStatus.ACTIVE);
        saveDoctor(a, "1002", "Cardiologist", EmploymentStatus.ACTIVE); // second active doctor: DISTINCT must collapse
        saveDoctor(b, "1003", "Neurologist", otherStatus());
        em.clear();

        List<Department> result = deptRepo.findDepartmentsWithDoctorStatus(EmploymentStatus.ACTIVE);

        assertEquals(1, result.size());
        assertEquals("Cardiology", result.get(0).getDeptName());
    }

    @Test
    void findDepartmentsBySpecialisation_returnsEachMatchingDepartmentOnce() {
        Department a = saveDept("Cardiology", "Block A");
        Department b = saveDept("Neurology", "Block B");
        saveDoctor(a, "1001", "Cardiologist", EmploymentStatus.ACTIVE);
        saveDoctor(a, "1002", "Cardiologist", EmploymentStatus.ACTIVE);
        saveDoctor(b, "1003", "Neurologist", EmploymentStatus.ACTIVE);
        em.clear();

        List<Department> result = deptRepo.findDepartmentsBySpecialisation("Cardiologist");

        assertEquals(1, result.size());
        assertEquals("Cardiology", result.get(0).getDeptName());
    }

    // Needs "WHERE dep.doctors IS EMPTY" in the query (IS NULL does not work on a collection).
    @Test
    void findDepartmentsWithoutDoctors_returnsOnlyEmptyDepartments() {
        Department a = saveDept("Cardiology", "Block A");
        saveDept("Radiology", "Annex");
        saveDoctor(a, "1001", "Cardiologist", EmploymentStatus.ACTIVE);
        em.clear();

        List<Department> result = deptRepo.findDepartmentsWithoutDoctors();

        assertEquals(Set.of("Radiology"), names(result));
    }

    // ---------- constraints and cascade ----------

    @Test
    void save_duplicateDeptName_violatesUniqueConstraint() {
        saveDept("Cardiology", "Block A");

        assertThrows(DataIntegrityViolationException.class, () -> saveDept("Cardiology", "Block B"));
    }

    @Test
    void delete_department_cascadesToItsDoctors() {
        Department a = saveDept("Cardiology", "Block A");
        saveDoctor(a, "1001", "Cardiologist", EmploymentStatus.ACTIVE);
        saveDoctor(a, "1002", "Cardiologist", EmploymentStatus.ACTIVE);
        em.clear();

        deptRepo.delete(deptRepo.findById(a.getDeptId()).orElseThrow());
        deptRepo.flush();
        em.clear();

        assertEquals(0, docRepo.count());
        assertEquals(0, deptRepo.count());
    }
}
