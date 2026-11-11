package com.example.hsp_mgmt.service; // change to match your project

// Adjust these imports to your real packages
import com.example.hsp_mgmt.dto.DepartmentRequestDto;
import com.example.hsp_mgmt.dto.DepartmentResponseDto;
import com.example.hsp_mgmt.model.Department;
import com.example.hsp_mgmt.model.Doctor;
import com.example.hsp_mgmt.model.enums.EmploymentStatus;
import com.example.hsp_mgmt.repository.DepartmentRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
class DepartmentServiceImplTest {

    @Mock
    private DepartmentRepository deptRepo;

    @InjectMocks
    private DepartmentServiceImpl service;

    // ---------- helpers ----------

    private Department dept(Long id, String name, String location, String phone) {
        Department d = new Department();
        d.setDeptName(name);
        d.setLocation(location);
        d.setPhone(phone);
        ReflectionTestUtils.setField(d, "deptId", id); // id is generated, so set it directly
        return d;
    }

    private DepartmentRequestDto request(String name, String location, String phone) {
        DepartmentRequestDto r = new DepartmentRequestDto();
        r.setDeptName(name);
        r.setLocation(location);
        r.setPhone(phone);
        return r;
    }

    // ---------- create ----------

    @Test
    void createDepartment_savesRequestValuesAndReturnsDto() {
        when(deptRepo.save(any(Department.class))).thenReturn(dept(1L, "Cardiology", "Block A", "0123"));

        DepartmentResponseDto resp = service.createDepartment(request("Cardiology", "Block A", "0123"));

        ArgumentCaptor<Department> captor = ArgumentCaptor.forClass(Department.class);
        verify(deptRepo).save(captor.capture());
        assertEquals("Cardiology", captor.getValue().getDeptName());
        assertEquals("Block A", captor.getValue().getLocation());
        assertEquals("0123", captor.getValue().getPhone());

        assertEquals(1L, resp.getDeptId());
        assertEquals("Cardiology", resp.getDeptName());
        assertEquals(0, resp.getDoctorCount());
    }

    // ---------- update ----------

    @Test
    void updateDepartment_changesAllFields() {
        Department existing = dept(1L, "Old", "Old loc", "111");
        when(deptRepo.findById(1L)).thenReturn(Optional.of(existing));
        when(deptRepo.save(existing)).thenReturn(existing);

        DepartmentResponseDto resp = service.updateDepartment(1L, request("Neurology", "Block B", "222"));

        assertEquals("Neurology", existing.getDeptName());
        assertEquals("Block B", existing.getLocation());
        assertEquals("222", existing.getPhone());
        assertEquals("Neurology", resp.getDeptName());
    }

    @Test
    void updateDepartment_notFound_throwsAndDoesNotSave() {
        when(deptRepo.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.updateDepartment(99L, request("X", "Y", "Z")));

        assertEquals("Department not found", ex.getMessage());
        verify(deptRepo, never()).save(any());
    }

    // ---------- read ----------

    @Test
    void getDepartmentById_returnsDto() {
        when(deptRepo.findById(1L)).thenReturn(Optional.of(dept(1L, "Cardiology", "Block A", "0123")));

        DepartmentResponseDto resp = service.getDepartmentById(1L);

        assertEquals(1L, resp.getDeptId());
        assertEquals("Cardiology", resp.getDeptName());
        assertEquals("Block A", resp.getLocation());
        assertEquals("0123", resp.getPhone());
    }

    @Test
    void getDepartmentById_notFound_throws() {
        when(deptRepo.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.getDepartmentById(99L));
        assertEquals("Department not found", ex.getMessage());
    }

    @Test
    void getDepartmentById_doctorCountMatchesDoctorsInDepartment() {
        Department d = dept(1L, "Cardiology", "Block A", "0123");
        d.getDoctors().add(new Doctor());
        d.getDoctors().add(new Doctor());
        when(deptRepo.findById(1L)).thenReturn(Optional.of(d));

        assertEquals(2, service.getDepartmentById(1L).getDoctorCount());
    }

    @Test
    void getAllDepartments_mapsEveryDepartment() {
        when(deptRepo.findAll()).thenReturn(List.of(
                dept(1L, "Cardiology", "Block A", "1"),
                dept(2L, "Neurology", "Block B", "2")));

        List<DepartmentResponseDto> result = service.getAllDepartments();

        assertEquals(2, result.size());
        assertEquals("Cardiology", result.get(0).getDeptName());
        assertEquals("Neurology", result.get(1).getDeptName());
    }

    @Test
    void getAllDepartments_empty_returnsEmptyList() {
        when(deptRepo.findAll()).thenReturn(List.of());

        assertTrue(service.getAllDepartments().isEmpty());
    }

    // ---------- delete ----------

    @Test
    void deleteDepartment_deletesFoundEntity() {
        Department existing = dept(1L, "Cardiology", "Block A", "0123");
        when(deptRepo.findById(1L)).thenReturn(Optional.of(existing));

        service.deleteDepartment(1L);

        verify(deptRepo).delete(existing);
    }

    @Test
    void deleteDepartment_notFound_throwsAndDoesNotDelete() {
        when(deptRepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.deleteDepartment(99L));
        verify(deptRepo, never()).delete(any());
    }

    // ---------- search ----------

    @Test
    void getDepartmentByName_returnsDto() {
        when(deptRepo.findByDeptNameIgnoreCase("cardiology"))
                .thenReturn(Optional.of(dept(1L, "Cardiology", "Block A", "0123")));

        assertEquals("Cardiology", service.getDepartmentByName("cardiology").getDeptName());
    }

    @Test
    void getDepartmentByName_notFound_messageContainsName() {
        when(deptRepo.findByDeptNameIgnoreCase("Ghost")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.getDepartmentByName("Ghost"));
        assertTrue(ex.getMessage().contains("Ghost"));
    }

    @Test
    void getDepartmentsByLocation_mapsResults() {
        when(deptRepo.findByLocationContainingIgnoreCase("block"))
                .thenReturn(List.of(dept(1L, "Cardiology", "Block A", "1")));

        List<DepartmentResponseDto> result = service.getDepartmentsByLocation("block");

        assertEquals(1, result.size());
        assertEquals("Block A", result.get(0).getLocation());
    }

    @Test
    void getDepartmentsWithDoctorsByStatus_mapsResults() {
        when(deptRepo.findDepartmentsWithDoctorStatus(EmploymentStatus.ACTIVE))
                .thenReturn(List.of(dept(1L, "Cardiology", "Block A", "1")));

        assertEquals(1, service.getDepartmentsWithDoctorsByStatus(EmploymentStatus.ACTIVE).size());
    }

    @Test
    void getDepartmentsWithoutDoctors_mapsResults() {
        when(deptRepo.findDepartmentsWithoutDoctors())
                .thenReturn(List.of(dept(3L, "Empty dept", "Block C", "3")));

        List<DepartmentResponseDto> result = service.getDepartmentsWithoutDoctors();

        assertEquals(1, result.size());
        assertEquals(0, result.get(0).getDoctorCount());
    }

    @Test
    void getDepartmentsBySpecialisation_mapsResults() {
        when(deptRepo.findDepartmentsBySpecialisation("Cardiologist"))
                .thenReturn(List.of(dept(1L, "Cardiology", "Block A", "1")));

        assertEquals(1, service.getDepartmentsBySpecialisation("Cardiologist").size());
    }

    // ---------- partial updates ----------

    @Test
    void updateDepartmentLocation_changesOnlyLocation() {
        Department existing = dept(1L, "Cardiology", "Block A", "0123");
        when(deptRepo.findById(1L)).thenReturn(Optional.of(existing));
        when(deptRepo.save(existing)).thenReturn(existing);

        DepartmentResponseDto resp = service.updateDepartmentLocation(1L, "Block Z");

        assertEquals("Block Z", resp.getLocation());
        assertEquals("Cardiology", existing.getDeptName());
        assertEquals("0123", existing.getPhone());
    }

    @Test
    void updateDepartmentLocation_notFound_throwsAndDoesNotSave() {
        when(deptRepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.updateDepartmentLocation(99L, "Block Z"));
        verify(deptRepo, never()).save(any());
    }

    @Test
    void updateDepartmentPhone_changesOnlyPhone() {
        Department existing = dept(1L, "Cardiology", "Block A", "0123");
        when(deptRepo.findById(1L)).thenReturn(Optional.of(existing));
        when(deptRepo.save(existing)).thenReturn(existing);

        DepartmentResponseDto resp = service.updateDepartmentPhone(1L, "999");

        assertEquals("999", resp.getPhone());
        assertEquals("Cardiology", existing.getDeptName());
        assertEquals("Block A", existing.getLocation());
    }

    @Test
    void updateDepartmentPhone_notFound_throwsAndDoesNotSave() {
        when(deptRepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.updateDepartmentPhone(99L, "999"));
        verify(deptRepo, never()).save(any());
    }
}
