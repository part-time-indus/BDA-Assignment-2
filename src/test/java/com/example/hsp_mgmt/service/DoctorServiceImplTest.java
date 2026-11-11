package com.example.hsp_mgmt.service;

import com.example.hsp_mgmt.dto.DoctorRequestDto;
import com.example.hsp_mgmt.dto.DoctorResponseDto;
import com.example.hsp_mgmt.model.Department;
import com.example.hsp_mgmt.model.Doctor;
import com.example.hsp_mgmt.model.enums.EmploymentStatus;
import com.example.hsp_mgmt.model.enums.Gender;
import com.example.hsp_mgmt.repository.DepartmentRepository;
import com.example.hsp_mgmt.repository.DoctorRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class DoctorServiceImplTest {

    @Mock
    private DoctorRepository docRepo;

    @Mock
    private DepartmentRepository depRepo;

    @InjectMocks
    private DoctorServiceImpl service;

    // ---------- helpers ----------

    private Department dept(Long id, String name, String location) {
        Department d = new Department();
        d.setDeptName(name);
        d.setLocation(location);
        ReflectionTestUtils.setField(d, "deptId", id);
        return d;
    }

    private Doctor doctor(Long id, Department dept, EmploymentStatus status) {
        Doctor d = new Doctor();
        d.setFirstName("Asha");
        d.setLastName("Verma");
        d.setPhone("9876543210");
        d.setEmail("asha@example.com");
        d.setSpecialisation("Cardiologist");
        d.setGender(anyGender());
        d.setJoinDate(LocalDate.of(2020, 1, 15));
        d.setConsultationFee(new BigDecimal("750.00"));
        d.setStatus(status);
        d.setDepartment(dept);
        ReflectionTestUtils.setField(d, "doctorId", id);
        return d;
    }

    // Enum constants other than ACTIVE are unknown to me, so pick them dynamically
    private Gender anyGender() {
        return Gender.values()[0];
    }

    private EmploymentStatus otherStatus() {
        return Arrays.stream(EmploymentStatus.values())
                .filter(s -> s != EmploymentStatus.ACTIVE)
                .findFirst()
                .orElseThrow();
    }

    private DoctorRequestDto request(Long deptId) {
        DoctorRequestDto r = new DoctorRequestDto();
        r.setFirstName("Ravi");
        r.setLastName("Kumar");
        r.setGender(anyGender());
        r.setPhone("1112223334");
        r.setEmail("ravi@example.com");
        r.setSplz("Neurologist");
        r.setDepartmentId(deptId);
        r.setJoinDate(LocalDate.of(2022, 3, 1));
        r.setConsultationFee(new BigDecimal("900.00"));
        r.setStatus(otherStatus());
        return r;
    }

    // ---------- add ----------

    // NOTE: fails against the current addDoctor(): it copies gender, joinDate, phone,
    // specialisation and status from the new empty Doctor instead of from the request.
    @Test
    void addDoctor_copiesAllFieldsFromRequest() {
        Department cardiology = dept(2L, "Cardiology", "Block A");
        DoctorRequestDto req = request(2L);
        when(depRepo.findById(2L)).thenReturn(Optional.of(cardiology));
        when(docRepo.save(any(Doctor.class))).thenAnswer(inv -> inv.getArgument(0));

        DoctorResponseDto resp = service.addDoctor(req);

        ArgumentCaptor<Doctor> captor = ArgumentCaptor.forClass(Doctor.class);
        verify(docRepo).save(captor.capture());
        Doctor saved = captor.getValue();
        assertEquals("Ravi", saved.getFirstName());
        assertEquals("Kumar", saved.getLastName());
        assertEquals(req.getGender(), saved.getGender());
        assertEquals("1112223334", saved.getPhone());
        assertEquals("ravi@example.com", saved.getEmail());
        assertEquals("Neurologist", saved.getSpecialisation());
        assertEquals(LocalDate.of(2022, 3, 1), saved.getJoinDate());
        assertEquals(new BigDecimal("900.00"), saved.getConsultationFee());
        assertEquals(req.getStatus(), saved.getStatus());
        assertSame(cardiology, saved.getDepartment());

        assertEquals("Cardiology", resp.getDeptName());
    }

    @Test
    void addDoctor_departmentNotFound_throwsAndDoesNotSave() {
        when(depRepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.addDoctor(request(99L)));
        verify(docRepo, never()).save(any());
    }

    // ---------- update ----------

    @Test
    void updateDoctor_changesAllFieldsAndDepartment() {
        Department oldDept = dept(1L, "Old", "Block O");
        Department newDept = dept(2L, "Neurology", "Block B");
        Doctor existing = doctor(5L, oldDept, EmploymentStatus.ACTIVE);
        DoctorRequestDto req = request(2L);
        when(docRepo.findById(5L)).thenReturn(Optional.of(existing));
        when(depRepo.findById(2L)).thenReturn(Optional.of(newDept));
        when(docRepo.save(existing)).thenReturn(existing);

        DoctorResponseDto resp = service.updateDoctor(5L, req);

        assertEquals("Ravi", existing.getFirstName());
        assertEquals("Kumar", existing.getLastName());
        assertEquals("1112223334", existing.getPhone());
        assertEquals("ravi@example.com", existing.getEmail());
        assertEquals("Neurologist", existing.getSpecialisation());
        assertEquals(LocalDate.of(2022, 3, 1), existing.getJoinDate());
        assertEquals(new BigDecimal("900.00"), existing.getConsultationFee());
        assertEquals(req.getStatus(), existing.getStatus());
        assertSame(newDept, existing.getDepartment());
        assertEquals("Neurology", resp.getDeptName());
    }

    @Test
    void updateDoctor_doctorNotFound_throwsAndDoesNotSave() {
        when(docRepo.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.updateDoctor(99L, request(2L)));
        assertEquals("Doctor not found", ex.getMessage());
        verify(docRepo, never()).save(any());
    }

    @Test
    void updateDoctor_departmentNotFound_throwsAndDoesNotSave() {
        when(docRepo.findById(5L)).thenReturn(Optional.of(doctor(5L, dept(1L, "Old", "Block O"), EmploymentStatus.ACTIVE)));
        when(depRepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.updateDoctor(5L, request(99L)));
        verify(docRepo, never()).save(any());
    }

    // ---------- read ----------

    @Test
    void getDoctorById_mapsAllFieldsToDto() {
        Department cardiology = dept(2L, "Cardiology", "Block A");
        Doctor d = doctor(5L, cardiology, EmploymentStatus.ACTIVE);
        when(docRepo.findById(5L)).thenReturn(Optional.of(d));

        DoctorResponseDto resp = service.getDoctorById(5L);

        assertEquals(5L, resp.getDoctorId());
        assertEquals("Asha", resp.getFirstName());
        assertEquals("Verma", resp.getLastName());
        assertEquals("9876543210", resp.getPhone());
        assertEquals("asha@example.com", resp.getEmail());
        assertEquals("Cardiologist", resp.getSpecialisation()); // also checks the setSecialization typo in toDto
        assertEquals(2L, resp.getDeptId());
        assertEquals("Cardiology", resp.getDeptName());
        assertEquals("Block A", resp.getDeptLocation());
        assertEquals(LocalDate.of(2020, 1, 15), resp.getJoinDate());
        assertEquals(new BigDecimal("750.00"), resp.getConsultationFee());
        assertEquals(EmploymentStatus.ACTIVE, resp.getStatus());
        assertEquals(anyGender(), resp.getGender());
    }

    @Test
    void getDoctorById_notFound_throws() {
        when(docRepo.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.getDoctorById(99L));
        assertEquals("Doctor not found", ex.getMessage());
    }

    @Test
    void getAllDoctors_mapsEveryDoctor() {
        Department d = dept(1L, "Cardiology", "Block A");
        when(docRepo.findAll()).thenReturn(List.of(
                doctor(1L, d, EmploymentStatus.ACTIVE),
                doctor(2L, d, otherStatus())));

        assertEquals(2, service.getAllDoctors().size());
    }

    @Test
    void getAllDoctors_empty_returnsEmptyList() {
        when(docRepo.findAll()).thenReturn(List.of());

        assertTrue(service.getAllDoctors().isEmpty());
    }

    // ---------- delete ----------

    @Test
    void deleteDoctor_deletesFoundEntity() {
        Doctor d = doctor(5L, dept(1L, "Cardiology", "Block A"), EmploymentStatus.ACTIVE);
        when(docRepo.findById(5L)).thenReturn(Optional.of(d));

        service.deleteDoctor(5L);

        verify(docRepo).delete(d);
    }

    @Test
    void deleteDoctor_notFound_throwsAndDoesNotDelete() {
        when(docRepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.deleteDoctor(99L));
        verify(docRepo, never()).delete(any());
    }

    // ---------- search ----------

    @Test
    void getDoctorByPhone_returnsDto() {
        when(docRepo.findByPhone("9876543210"))
                .thenReturn(Optional.of(doctor(5L, dept(1L, "Cardiology", "Block A"), EmploymentStatus.ACTIVE)));

        assertEquals("9876543210", service.getDoctorByPhone("9876543210").getPhone());
    }

    @Test
    void getDoctorByPhone_notFound_messageContainsPhone() {
        when(docRepo.findByPhone("000")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.getDoctorByPhone("000"));
        assertTrue(ex.getMessage().contains("000"));
    }

    @Test
    void getDoctorByEmail_returnsDto() {
        when(docRepo.findByEmailIgnoreCase("ASHA@example.com"))
                .thenReturn(Optional.of(doctor(5L, dept(1L, "Cardiology", "Block A"), EmploymentStatus.ACTIVE)));

        assertEquals("asha@example.com", service.getDoctorByEmail("ASHA@example.com").getEmail());
    }

    @Test
    void getDoctorByEmail_notFound_messageContainsEmail() {
        when(docRepo.findByEmailIgnoreCase("none@example.com")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.getDoctorByEmail("none@example.com"));
        assertTrue(ex.getMessage().contains("none@example.com"));
    }

    @Test
    void getDoctorsBySpecialisation_mapsResults() {
        Department d = dept(1L, "Cardiology", "Block A");
        when(docRepo.findBySpecialisationIgnoreCase("cardiologist"))
                .thenReturn(List.of(doctor(1L, d, EmploymentStatus.ACTIVE)));

        assertEquals(1, service.getDoctorsBySpecialisation("cardiologist").size());
    }

    @Test
    void getDoctorsByDepartment_looksUpDepartmentThenSearchesByItsName() {
        Department d = dept(2L, "Cardiology", "Block A");
        when(depRepo.findById(2L)).thenReturn(Optional.of(d));
        when(docRepo.findDoctorsByDepartmentName("Cardiology"))
                .thenReturn(List.of(doctor(1L, d, EmploymentStatus.ACTIVE)));

        assertEquals(1, service.getDoctorsByDepartment(2L).size());
    }

    @Test
    void getDoctorsByDepartment_departmentNotFound_throwsWithoutQueryingDoctors() {
        when(depRepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.getDoctorsByDepartment(99L));
        verify(docRepo, never()).findDoctorsByDepartmentName(any());
    }

    @Test
    void getDoctorsByDepartmentName_mapsResults() {
        Department d = dept(2L, "Cardiology", "Block A");
        when(docRepo.findDoctorsByDepartmentName("Cardiology"))
                .thenReturn(List.of(doctor(1L, d, EmploymentStatus.ACTIVE)));

        assertEquals(1, service.getDoctorsByDepartmentName("Cardiology").size());
    }

    @Test
    void getDoctorsByDepartmentAndStatus_returnsOnlyMatchingStatus() {
        Department d = dept(2L, "Cardiology", "Block A");
        Doctor active = doctor(1L, d, EmploymentStatus.ACTIVE);
        Doctor other = doctor(2L, d, otherStatus());
        when(depRepo.findById(2L)).thenReturn(Optional.of(d));
        when(docRepo.findDeptId(2L)).thenReturn(List.of(active, other));

        List<DoctorResponseDto> result = service.getDoctorsByDepartmentAndStatus(2L, EmploymentStatus.ACTIVE);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getDoctorId());
    }

    @Test
    void getDoctorsByDepartmentAndStatus_departmentNotFound_throws() {
        when(depRepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> service.getDoctorsByDepartmentAndStatus(99L, EmploymentStatus.ACTIVE));
        verify(docRepo, never()).findDeptId(any());
    }

    // ---------- partial updates ----------

    @Test
    void updateDoctorStatus_changesOnlyStatus() {
        Doctor d = doctor(5L, dept(1L, "Cardiology", "Block A"), EmploymentStatus.ACTIVE);
        when(docRepo.findById(5L)).thenReturn(Optional.of(d));
        when(docRepo.save(d)).thenReturn(d);

        DoctorResponseDto resp = service.updateDoctorStatus(5L, otherStatus());

        assertEquals(otherStatus(), resp.getStatus());
        assertEquals("Asha", d.getFirstName());
        assertEquals(new BigDecimal("750.00"), d.getConsultationFee());
    }

    @Test
    void updateDoctorStatus_notFound_throwsAndDoesNotSave() {
        when(docRepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.updateDoctorStatus(99L, EmploymentStatus.ACTIVE));
        verify(docRepo, never()).save(any());
    }

    @Test
    void updateConsultationFee_changesOnlyFee() {
        Doctor d = doctor(5L, dept(1L, "Cardiology", "Block A"), EmploymentStatus.ACTIVE);
        when(docRepo.findById(5L)).thenReturn(Optional.of(d));
        when(docRepo.save(d)).thenReturn(d);

        DoctorResponseDto resp = service.updateConsultationFee(5L, new BigDecimal("1200.50"));

        assertEquals(new BigDecimal("1200.50"), resp.getConsultationFee());
        assertEquals(EmploymentStatus.ACTIVE, d.getStatus());
    }

    @Test
    void updateConsultationFee_notFound_throwsAndDoesNotSave() {
        when(docRepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.updateConsultationFee(99L, BigDecimal.TEN));
        verify(docRepo, never()).save(any());
    }

    @Test
    void updateDoctorDepartment_movesDoctorToNewDepartment() {
        Department oldDept = dept(1L, "Old", "Block O");
        Department newDept = dept(2L, "Neurology", "Block B");
        Doctor d = doctor(5L, oldDept, EmploymentStatus.ACTIVE);
        when(docRepo.findById(5L)).thenReturn(Optional.of(d));
        when(depRepo.findById(2L)).thenReturn(Optional.of(newDept));
        when(docRepo.save(d)).thenReturn(d);

        DoctorResponseDto resp = service.updateDoctorDepartment(5L, 2L);

        assertSame(newDept, d.getDepartment());
        assertEquals(2L, resp.getDeptId());
        assertEquals("Neurology", resp.getDeptName());
    }

    @Test
    void updateDoctorDepartment_doctorNotFound_throwsAndDoesNotSave() {
        when(docRepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.updateDoctorDepartment(99L, 2L));
        verify(docRepo, never()).save(any());
    }

    @Test
    void updateDoctorDepartment_departmentNotFound_throwsAndDoesNotSave() {
        Doctor d = doctor(5L, dept(1L, "Old", "Block O"), EmploymentStatus.ACTIVE);
        when(docRepo.findById(5L)).thenReturn(Optional.of(d));
        when(depRepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.updateDoctorDepartment(5L, 99L));
        verify(docRepo, never()).save(any());
    }
}
