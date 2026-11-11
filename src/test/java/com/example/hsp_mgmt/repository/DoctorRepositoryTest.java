package com.example.hsp_mgmt.repository; 

import com.example.hsp_mgmt.model.Department;
import com.example.hsp_mgmt.model.Doctor;
import com.example.hsp_mgmt.model.enums.EmploymentStatus;
import com.example.hsp_mgmt.model.enums.Gender;


import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import org.hibernate.Hibernate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
class DoctorRepositoryTest {

    @Autowired
    private DoctorRepository docRepo;

    @Autowired
    private DepartmentRepository deptRepo;

    @Autowired
    private EntityManager em;

    // ---------- helpers ----------

    private Department saveDept(String name) {
        Department d = new Department();
        d.setDeptName(name);
        d.setLocation("Block A");
        d.setPhone("0123456789");
        return deptRepo.saveAndFlush(d);
    }

    private Doctor newDoctor(Department dept, String phone, String email, String specialisation) {
        Doctor d = new Doctor();
        d.setFirstName("First");
        d.setLastName("Last");
        d.setGender(Gender.values()[0]);
        d.setPhone(phone);
        d.setEmail(email);
        d.setSpecialisation(specialisation);
        d.setDepartment(dept);
        d.setConsultationFee(new BigDecimal("500.00"));
        d.setJoinDate(LocalDate.of(2020, 1, 1));
        d.setStatus(EmploymentStatus.ACTIVE);
        return d;
    }

    private Doctor saveDoctor(Department dept, String phone, String email, String specialisation) {
        return docRepo.saveAndFlush(newDoctor(dept, phone, email, specialisation));
    }

    private Set<String> phones(List<Doctor> list) {
        return list.stream().map(Doctor::getPhone).collect(Collectors.toSet());
    }

    // ---------- derived queries ----------

    @Test
    void findByPhone_returnsMatchingDoctor() {
        saveDoctor(saveDept("Cardiology"), "9876543210", "a@example.com", "Cardiologist");
        em.clear();

        Optional<Doctor> found = docRepo.findByPhone("9876543210");

        assertTrue(found.isPresent());
        assertEquals("a@example.com", found.get().getEmail());
    }

    @Test
    void findByPhone_unknownPhone_returnsEmpty() {
        assertTrue(docRepo.findByPhone("000").isEmpty());
    }

    @Test
    void findByEmailIgnoreCase_ignoresCase() {
        saveDoctor(saveDept("Cardiology"), "1001", "asha@example.com", "Cardiologist");
        em.clear();

        assertTrue(docRepo.findByEmailIgnoreCase("ASHA@Example.COM").isPresent());
        assertTrue(docRepo.findByEmailIgnoreCase("other@example.com").isEmpty());
    }

    @Test
    void findBySpecialisationIgnoreCase_returnsAllMatches() {
        Department d = saveDept("Cardiology");
        saveDoctor(d, "1001", "a@example.com", "Cardiologist");
        saveDoctor(d, "1002", "b@example.com", "Cardiologist");
        saveDoctor(d, "1003", "c@example.com", "Neurologist");
        em.clear();

        assertEquals(Set.of("1001", "1002"), phones(docRepo.findBySpecialisationIgnoreCase("cardiologist")));
    }

    // ---------- @Query methods ----------

    @Test
    void findDeptId_returnsOnlyDoctorsOfThatDepartment() {
        Department cardiology = saveDept("Cardiology");
        Department neurology = saveDept("Neurology");
        saveDoctor(cardiology, "1001", "a@example.com", "Cardiologist");
        saveDoctor(cardiology, "1002", "b@example.com", "Cardiologist");
        saveDoctor(neurology, "1003", "c@example.com", "Neurologist");
        em.clear();

        assertEquals(Set.of("1001", "1002"), phones(docRepo.findDeptId(cardiology.getDeptId())));
    }

    @Test
    void findAllDoctorsWithDepartment_returnsAllDoctorsWithDepartmentLoaded() {
        Department cardiology = saveDept("Cardiology");
        Department neurology = saveDept("Neurology");
        saveDoctor(cardiology, "1001", "a@example.com", "Cardiologist");
        saveDoctor(neurology, "1002", "b@example.com", "Neurologist");
        em.clear();

        List<Doctor> result = docRepo.findAllDoctorsWithDepartment();

        assertEquals(2, result.size());
        for (Doctor d : result) {
            assertTrue(Hibernate.isInitialized(d.getDepartment()));
            assertNotNull(d.getDepartment().getDeptName());
        }
    }

    // Needs "WHERE d.department.deptName = :deptName" in the query (comparing the entity to a String fails).
    @Test
    @Transactional 
    void findDoctorsByDepartmentName_returnsOnlyDoctorsOfThatDepartment() {
        Department cardiology = saveDept("Cardiology");
        Department neurology = saveDept("Neurology");
        saveDoctor(cardiology, "1001", "a@example.com", "Cardiologist");
        saveDoctor(neurology, "1002", "b@example.com", "Neurologist");
        em.clear();

        assertEquals(Set.of("1001"), phones(docRepo.findDoctorsByDepartmentName("Cardiology")));
        assertTrue(docRepo.findDoctorsByDepartmentName("Ghost").isEmpty());
    }

    // ---------- constraints and defaults ----------

    @Test
    void save_duplicatePhone_violatesUniqueConstraint() {
        Department d = saveDept("Cardiology");
        saveDoctor(d, "1001", "a@example.com", "Cardiologist");

        assertThrows(DataIntegrityViolationException.class,
                () -> saveDoctor(d, "1001", "b@example.com", "Cardiologist"));
    }

    @Test
    void save_duplicateEmail_violatesUniqueConstraint() {
        Department d = saveDept("Cardiology");
        saveDoctor(d, "1001", "same@example.com", "Cardiologist");

        assertThrows(DataIntegrityViolationException.class,
                () -> saveDoctor(d, "1002", "same@example.com", "Cardiologist"));
    }

    @Test
    void save_severalDoctorsWithoutEmail_isAllowed() {
        Department d = saveDept("Cardiology");

        saveDoctor(d, "1001", null, "Cardiologist");
        saveDoctor(d, "1002", null, "Cardiologist");

        assertEquals(2, docRepo.count());
    }

    @Test
    void save_negativeFee_violatesCheckConstraint() {
        Department d = saveDept("Cardiology");
        Doctor doc = newDoctor(d, "1001", "a@example.com", "Cardiologist");
        doc.setConsultationFee(new BigDecimal("-1.00"));

        assertThrows(DataIntegrityViolationException.class, () -> docRepo.saveAndFlush(doc));
    }

    @Test
    void save_withoutFeeAndStatus_usesEntityDefaults() {
        Department d = saveDept("Cardiology");
        Doctor doc = new Doctor();
        doc.setFirstName("First");
        doc.setLastName("Last");
        doc.setGender(Gender.values()[0]);
        doc.setPhone("1001");
        doc.setSpecialisation("Cardiologist");
        doc.setDepartment(d);
        doc.setJoinDate(LocalDate.of(2020, 1, 1));
        Long id = docRepo.saveAndFlush(doc).getDoctorId();
        em.clear();

        Doctor reloaded = docRepo.findById(id).orElseThrow();

        assertEquals(0, new BigDecimal("500").compareTo(reloaded.getConsultationFee()));
        assertEquals(EmploymentStatus.ACTIVE, reloaded.getStatus());
    }
}
