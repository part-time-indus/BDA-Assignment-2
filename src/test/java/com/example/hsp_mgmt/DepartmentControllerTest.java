package com.example.hsp_mgmt; // change to match your project

import com.example.hsp_mgmt.controller.DepartmentController;
// Adjust these imports to your real packages
import com.example.hsp_mgmt.dto.DepartmentRequestDto;
import com.example.hsp_mgmt.dto.DepartmentResponseDto;
import com.example.hsp_mgmt.model.enums.EmploymentStatus;
import com.example.hsp_mgmt.service.DepartmentService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@ExtendWith(MockitoExtension.class)
class DepartmentControllerTest {

    @Mock
    private DepartmentService deptService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver("/WEB-INF/views/", ".jsp");
        mockMvc = MockMvcBuilders.standaloneSetup(new DepartmentController(deptService))
                .setViewResolvers(resolver)
                .build();
    }

    // ---------- create ----------

    @Test
    void showCreateForm_returnsFormViewWithEmptyForm() throws Exception {
        mockMvc.perform(get("/departments/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("create-department"))
                .andExpect(model().attributeExists("departmentForm"));
    }

    @Test
    void createDepartment_bindsFormCallsServiceAndRedirects() throws Exception {
        mockMvc.perform(post("/departments")
                        .param("deptName", "Cardiology")
                        .param("location", "Block A")
                        .param("phone", "0123456789"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/departments"));

        ArgumentCaptor<DepartmentRequestDto> captor = ArgumentCaptor.forClass(DepartmentRequestDto.class);
        verify(deptService).createDepartment(captor.capture());
        assertEquals("Cardiology", captor.getValue().getDeptName());
        assertEquals("Block A", captor.getValue().getLocation());
        assertEquals("0123456789", captor.getValue().getPhone());
    }

    // ---------- update ----------

    @Test
    void showUpdateForm_prefillsFormFromExistingDepartment() throws Exception {
        DepartmentResponseDto existing = mock(DepartmentResponseDto.class);
        when(existing.getDeptName()).thenReturn("Cardiology");
        when(existing.getLocation()).thenReturn("Block A");
        when(existing.getPhone()).thenReturn("0123456789");
        when(deptService.getDepartmentById(1L)).thenReturn(existing);

        MvcResult result = mockMvc.perform(get("/departments/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("update-department"))
                .andExpect(model().attribute("deptId", 1L))
                .andReturn();

        DepartmentRequestDto form =
                (DepartmentRequestDto) result.getModelAndView().getModel().get("departmentForm");
        assertEquals("Cardiology", form.getDeptName());
        assertEquals("Block A", form.getLocation());
        assertEquals("0123456789", form.getPhone());
    }

    @Test
    void updateDepartment_callsServiceAndRedirects() throws Exception {
        mockMvc.perform(post("/departments/5")
                        .param("deptName", "Neurology")
                        .param("location", "Block B")
                        .param("phone", "999"))
                .andExpect(redirectedUrl("/departments"));

        ArgumentCaptor<DepartmentRequestDto> captor = ArgumentCaptor.forClass(DepartmentRequestDto.class);
        verify(deptService).updateDepartment(eq(5L), captor.capture());
        assertEquals("Neurology", captor.getValue().getDeptName());
    }

    // ---------- read ----------

    @Test
    void getDepartmentById_putsDepartmentInModel() throws Exception {
        DepartmentResponseDto dept = mock(DepartmentResponseDto.class);
        when(deptService.getDepartmentById(3L)).thenReturn(dept);

        mockMvc.perform(get("/departments/3"))
                .andExpect(status().isOk())
                .andExpect(view().name("department-detail"))
                .andExpect(model().attribute("department", dept));
    }

    @Test
    void getAllDepartments_putsListInModel() throws Exception {
        List<DepartmentResponseDto> list = List.of(mock(DepartmentResponseDto.class));
        when(deptService.getAllDepartments()).thenReturn(list);

        mockMvc.perform(get("/departments"))
                .andExpect(status().isOk())
                .andExpect(view().name("departments-list"))
                .andExpect(model().attribute("departmentList", list));
    }

    @Test
    void getDepartmentByName_returnsDetailView() throws Exception {
        DepartmentResponseDto dept = mock(DepartmentResponseDto.class);
        when(deptService.getDepartmentByName("Cardiology")).thenReturn(dept);

        mockMvc.perform(get("/departments/name").param("deptName", "Cardiology"))
                .andExpect(view().name("department-detail"))
                .andExpect(model().attribute("department", dept));
    }

    @Test
    void getDepartmentByLocation_returnsListView() throws Exception {
        List<DepartmentResponseDto> list = List.of(mock(DepartmentResponseDto.class));
        when(deptService.getDepartmentsByLocation("Block A")).thenReturn(list);

        mockMvc.perform(get("/departments/location").param("location", "Block A"))
                .andExpect(view().name("departments-list"))
                .andExpect(model().attribute("departmentList", list));
    }

    @Test
    void getDepartmentsWithDoctorsByStatus_convertsEnumParam() throws Exception {
        List<DepartmentResponseDto> list = List.of(mock(DepartmentResponseDto.class));
        when(deptService.getDepartmentsWithDoctorsByStatus(EmploymentStatus.ACTIVE)).thenReturn(list);

        mockMvc.perform(get("/departments/doctors/status").param("status", "ACTIVE"))
                .andExpect(view().name("departments-list"))
                .andExpect(model().attribute("departmentList", list));
    }

    @Test
    void getDepartmentsWithoutDoctors_returnsListView() throws Exception {
        List<DepartmentResponseDto> list = List.of(mock(DepartmentResponseDto.class));
        when(deptService.getDepartmentsWithoutDoctors()).thenReturn(list);

        mockMvc.perform(get("/departments/doctors/empty"))
                .andExpect(view().name("departments-list"))
                .andExpect(model().attribute("departmentList", list));
    }

    @Test
    void getDepartmentsBySpecialisation_returnsListView() throws Exception {
        List<DepartmentResponseDto> list = List.of(mock(DepartmentResponseDto.class));
        when(deptService.getDepartmentsBySpecialisation("Cardiologist")).thenReturn(list);

        mockMvc.perform(get("/departments/doctors/specialisation").param("specialisation", "Cardiologist"))
                .andExpect(view().name("departments-list"))
                .andExpect(model().attribute("departmentList", list));
    }

    // ---------- delete / partial updates ----------

    @Test
    void deleteDepartment_callsServiceAndRedirects() throws Exception {
        mockMvc.perform(post("/departments/delete/7"))
                .andExpect(redirectedUrl("/departments"));

        verify(deptService).deleteDepartment(7L);
    }

    @Test
    void updateDepartmentLocation_redirectsToDetailPage() throws Exception {
        mockMvc.perform(post("/departments/4/location").param("location", "Block C"))
                .andExpect(redirectedUrl("/departments/4"));

        verify(deptService).updateDepartmentLocation(4L, "Block C");
    }

    @Test
    void updateDepartmentPhone_redirectsToDetailPage() throws Exception {
        mockMvc.perform(post("/departments/4/phone").param("phone", "555"))
                .andExpect(redirectedUrl("/departments/4"));

        verify(deptService).updateDepartmentPhone(4L, "555");
    }

    @Test
    void createDepartment_neverCallsOtherServiceMethods() throws Exception {
        mockMvc.perform(post("/departments").param("deptName", "X").param("location", "Y"));

        verify(deptService).createDepartment(any(DepartmentRequestDto.class));
    }
}
