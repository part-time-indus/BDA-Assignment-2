package com.example.hsp_mgmt.controller; 
import com.example.hsp_mgmt.dto.DoctorRequestDto;
import com.example.hsp_mgmt.dto.DoctorResponseDto;
import com.example.hsp_mgmt.model.enums.EmploymentStatus;
import com.example.hsp_mgmt.service.DoctorService;

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

import java.math.BigDecimal;
import java.time.LocalDate;
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
class DoctorControllerTest {

    @Mock
    private DoctorService docService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver("/WEB-INF/views/", ".jsp");
        mockMvc = MockMvcBuilders.standaloneSetup(new DoctorController(docService))
                .setViewResolvers(resolver)
                .build();
    }

    // ---------- create ----------

    @Test
    void showCreateForm_returnsFormViewWithEmptyForm() throws Exception {
        mockMvc.perform(get("/doctors/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("create-doctor"))
                .andExpect(model().attributeExists("doctorForm"));
    }

    // gender and joinDate are left out on purpose: they depend on your enum values / date format settings
    @Test
    void createDoctor_bindsFormCallsServiceAndRedirects() throws Exception {
        mockMvc.perform(post("/doctors")
                        .param("firstName", "Asha")
                        .param("lastName", "Verma")
                        .param("phone", "9876543210")
                        .param("email", "asha@example.com")
                        .param("splz", "Cardiologist")
                        .param("departmentId", "2")
                        .param("consultationFee", "750.00")
                        .param("status", "ACTIVE"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/doctors"));

        ArgumentCaptor<DoctorRequestDto> captor = ArgumentCaptor.forClass(DoctorRequestDto.class);
        verify(docService).addDoctor(captor.capture());
        DoctorRequestDto sent = captor.getValue();
        assertEquals("Asha", sent.getFirstName());
        assertEquals("Verma", sent.getLastName());
        assertEquals("9876543210", sent.getPhone());
        assertEquals("asha@example.com", sent.getEmail());
        assertEquals("Cardiologist", sent.getSplz());
        assertEquals(2L, sent.getDepartmentId());
        assertEquals(new BigDecimal("750.00"), sent.getConsultationFee());
        assertEquals(EmploymentStatus.ACTIVE, sent.getStatus());
    }

    // ---------- update ----------

    @Test
    void showUpdateForm_prefillsFormFromExistingDoctor() throws Exception {
        DoctorResponseDto existing = mock(DoctorResponseDto.class);
        when(existing.getFirstName()).thenReturn("Asha");
        when(existing.getLastName()).thenReturn("Verma");
        when(existing.getPhone()).thenReturn("9876543210");
        when(existing.getEmail()).thenReturn("asha@example.com");
        when(existing.getSpecialisation()).thenReturn("Cardiologist");
        when(existing.getDeptId()).thenReturn(2L);
        when(existing.getConsultationFee()).thenReturn(new BigDecimal("750.00"));
        when(existing.getJoinDate()).thenReturn(LocalDate.of(2020, 1, 15));
        when(existing.getStatus()).thenReturn(EmploymentStatus.ACTIVE);
        when(docService.getDoctorById(1L)).thenReturn(existing);

        MvcResult result = mockMvc.perform(get("/doctors/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("update-doctor"))
                .andExpect(model().attribute("docId", 1L))
                .andReturn();

        DoctorRequestDto form = (DoctorRequestDto) result.getModelAndView().getModel().get("doctorForm");
        assertEquals("Asha", form.getFirstName());
        assertEquals("Verma", form.getLastName());
        assertEquals("Cardiologist", form.getSplz());
        assertEquals(2L, form.getDepartmentId());
        assertEquals(new BigDecimal("750.00"), form.getConsultationFee());
        assertEquals(LocalDate.of(2020, 1, 15), form.getJoinDate());
        assertEquals(EmploymentStatus.ACTIVE, form.getStatus());
    }

    @Test
    void updateDoctor_callsServiceAndRedirects() throws Exception {
        mockMvc.perform(post("/doctors/5")
                        .param("firstName", "Ravi")
                        .param("lastName", "Kumar"))
                .andExpect(redirectedUrl("/doctors"));

        ArgumentCaptor<DoctorRequestDto> captor = ArgumentCaptor.forClass(DoctorRequestDto.class);
        verify(docService).updateDoctor(eq(5L), captor.capture());
        assertEquals("Ravi", captor.getValue().getFirstName());
    }

    // ---------- read ----------

    @Test
    void getDoctorById_putsDoctorInModel() throws Exception {
        DoctorResponseDto doc = mock(DoctorResponseDto.class);
        when(docService.getDoctorById(3L)).thenReturn(doc);

        mockMvc.perform(get("/doctors/3"))
                .andExpect(status().isOk())
                .andExpect(view().name("doctor-detail"))
                .andExpect(model().attribute("doctor", doc));
    }

    @Test
    void getDoctors_putsListInModel() throws Exception {
        List<DoctorResponseDto> list = List.of(mock(DoctorResponseDto.class));
        when(docService.getAllDoctors()).thenReturn(list);

        mockMvc.perform(get("/doctors"))
                .andExpect(status().isOk())
                .andExpect(view().name("doctors-list"))
                .andExpect(model().attribute("doctorList", list));
    }

    @Test
    void getDoctorByPhone_returnsDetailView() throws Exception {
        DoctorResponseDto doc = mock(DoctorResponseDto.class);
        when(docService.getDoctorByPhone("9876543210")).thenReturn(doc);

        mockMvc.perform(get("/doctors/phone").param("phone", "9876543210"))
                .andExpect(view().name("doctor-detail"))
                .andExpect(model().attribute("doctor", doc));
    }

    @Test
    void getDoctorByEmail_returnsDetailView() throws Exception {
        DoctorResponseDto doc = mock(DoctorResponseDto.class);
        when(docService.getDoctorByEmail("asha@example.com")).thenReturn(doc);

        mockMvc.perform(get("/doctors/email").param("email", "asha@example.com"))
                .andExpect(view().name("doctor-detail"))
                .andExpect(model().attribute("doctor", doc));
    }

    @Test
    void getDoctorBySpecialisation_returnsListView() throws Exception {
        List<DoctorResponseDto> list = List.of(mock(DoctorResponseDto.class));
        when(docService.getDoctorsBySpecialisation("Cardiologist")).thenReturn(list);

        mockMvc.perform(get("/doctors/specialisation").param("specialisation", "Cardiologist"))
                .andExpect(view().name("doctors-list"))
                .andExpect(model().attribute("doctorList", list));
    }

    @Test
    void getDoctorsByDepartment_returnsListView() throws Exception {
        List<DoctorResponseDto> list = List.of(mock(DoctorResponseDto.class));
        when(docService.getDoctorsByDepartment(2L)).thenReturn(list);

        mockMvc.perform(get("/doctors/department/id").param("deptId", "2"))
                .andExpect(view().name("doctors-list"))
                .andExpect(model().attribute("doctorList", list));
    }

    @Test
    void getDoctorsByDepartmentName_returnsListView() throws Exception {
        List<DoctorResponseDto> list = List.of(mock(DoctorResponseDto.class));
        when(docService.getDoctorsByDepartmentName("Cardiology")).thenReturn(list);

        mockMvc.perform(get("/doctors/department/name").param("deptName", "Cardiology"))
                .andExpect(view().name("doctors-list"))
                .andExpect(model().attribute("doctorList", list));
    }

    @Test
    void getDoctorsByDepartmentAndStatus_convertsEnumParam() throws Exception {
        List<DoctorResponseDto> list = List.of(mock(DoctorResponseDto.class));
        when(docService.getDoctorsByDepartmentAndStatus(2L, EmploymentStatus.ACTIVE)).thenReturn(list);

        mockMvc.perform(get("/doctors/department/status")
                        .param("deptId", "2")
                        .param("status", "ACTIVE"))
                .andExpect(view().name("doctors-list"))
                .andExpect(model().attribute("doctorList", list));
    }

    // ---------- delete / partial updates ----------

    @Test
    void deleteDoctor_callsServiceAndRedirects() throws Exception {
        mockMvc.perform(post("/doctors/delete/7"))
                .andExpect(redirectedUrl("/doctors"));

        verify(docService).deleteDoctor(7L);
    }

    @Test
    void updateDoctorStatus_callsServiceAndRedirects() throws Exception {
        mockMvc.perform(post("/doctors/4/status").param("status", "ACTIVE"))
                .andExpect(redirectedUrl("/doctors"));

        verify(docService).updateDoctorStatus(4L, EmploymentStatus.ACTIVE);
    }

    @Test
    void updateConsultationFee_callsServiceAndRedirects() throws Exception {
        mockMvc.perform(post("/doctors/4/consultationFee").param("consultationFee", "900.50"))
                .andExpect(redirectedUrl("/doctors"));

        verify(docService).updateConsultationFee(4L, new BigDecimal("900.50"));
    }

    @Test
    void updateDoctorDepartment_callsServiceAndRedirects() throws Exception {
        mockMvc.perform(post("/doctors/4/department").param("deptId", "9"))
                .andExpect(redirectedUrl("/doctors"));

        verify(docService).updateDoctorDepartment(4L, 9L);
    }

    @Test
    void createDoctor_passesADoctorRequestDtoToService() throws Exception {
        mockMvc.perform(post("/doctors").param("firstName", "A"));

        verify(docService).addDoctor(any(DoctorRequestDto.class));
    }
}
