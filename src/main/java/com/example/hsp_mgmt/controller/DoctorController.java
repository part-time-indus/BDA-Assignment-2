package com.example.hsp_mgmt.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.hsp_mgmt.dto.DoctorRequestDto;
import com.example.hsp_mgmt.dto.DoctorResponseDto;
import com.example.hsp_mgmt.model.enums.EmploymentStatus;
import com.example.hsp_mgmt.service.DoctorService;

@Controller 
@RequestMapping ("/doctors")
public class DoctorController {
    
    
    private final DoctorService docService;


    public DoctorController(DoctorService docService){
        this.docService = docService;
    }


    @GetMapping("/new")
    public String showCreateForm(Model model){
        model.addAttribute("doctorForm", new DoctorRequestDto());
        return "create-doctor";
    }

    @PostMapping()
    public String createDoctor(@ModelAttribute("doctorForm") DoctorRequestDto req){
        docService.addDoctor(req);
        return "redirect:/doctors";
    }

    @GetMapping("/edit/{docId}")
    public String showUpdateForm(@PathVariable  Long docId, Model model){
        DoctorResponseDto resp = docService.getDoctorById(docId);
        DoctorRequestDto req = new DoctorRequestDto();
        req.setConsultationFee(resp.getConsultationFee());
        req.setDepartmentId(resp.getDeptId());
        req.setEmail(resp.getEmail());
        req.setFirstName(resp.getFirstName());
        req.setLastName(resp.getLastName());
        req.setGender(resp.getGender());
        req.setJoinDate(resp.getJoinDate());
        req.setPhone(resp.getPhone());
        req.setSplz(resp.getSpecialisation());
        req.setStatus(resp.getStatus());
        model.addAttribute("doctorForm", req);
        model.addAttribute("docId", docId);
        return "update-doctor";
    }

    @PostMapping("/{docId}")
    public String updateDoctor(@PathVariable("docId") Long docId, @ModelAttribute("doctorForm") DoctorRequestDto req){
        docService.updateDoctor(docId, req);
        return "redirect:/doctors";
    }

    @GetMapping("/{docId}")
    public String getDoctorById(@PathVariable("docId") Long docId, Model model){
        DoctorResponseDto resp = docService.getDoctorById(docId);
        model.addAttribute("doctor",resp);
        return "doctor-detail";
    }

    @GetMapping
    public String getDoctors(Model model){
        List<DoctorResponseDto> resp = docService.getAllDoctors();
        model.addAttribute("doctorList", resp);
        return "doctors-list";
    }

    @PostMapping("/delete/{docId}")
    public String deleteDoctor(@PathVariable("docId") Long docId){
        docService.deleteDoctor(docId);
        return "redirect:/doctors";
    }

    @GetMapping("/phone")
    public String getDoctorByPhone(@RequestParam("phone") String phone, Model model){
        DoctorResponseDto resp = docService.getDoctorByPhone(phone);
        model.addAttribute("doctor", resp);
        return "doctor-detail";        
    }

    @GetMapping("/email")
    public String getDoctorByEmail(@RequestParam("email") String email, Model model){
        DoctorResponseDto resp = docService.getDoctorByEmail(email);
        model.addAttribute("doctor", resp);
        return "doctor-detail";

    }

    @GetMapping ("/specialisation")
    public String getDoctorBySpecialisation(@RequestParam("specialisation") String specialisation, Model model){
        List<DoctorResponseDto> resp = docService.getDoctorsBySpecialisation(specialisation);
        model.addAttribute("doctorList", resp);
        return "doctors-list";
    }

    
    // getDoctorsByDepartment();

    @GetMapping("/department/id")
    public String getDoctorsByDepartment(@RequestParam("deptId") Long deptId, Model model){
        List<DoctorResponseDto> resp = docService.getDoctorsByDepartment(deptId);
        model.addAttribute("doctorList", resp);
        return "doctors-list";
    }

    // getDoctorsByDepartmentName();

    @GetMapping("/department/name")
    public String getDoctorsByDepartmentName(@RequestParam("deptName") String deptName, Model model){
        List<DoctorResponseDto> resp = docService.getDoctorsByDepartmentName(deptName);
        model.addAttribute("doctorList", resp);
        return "doctors-list";
    }

    // getDoctorByDepartmentAndStatus();

    @GetMapping("/department/status")
    public String getDoctorsByDepartmenAndStatus(@RequestParam("deptId") Long deptId, @RequestParam("status") EmploymentStatus status, Model model){
        List<DoctorResponseDto> resp = docService.getDoctorsByDepartmentAndStatus(deptId, status);
        model.addAttribute("doctorList",  resp);
        return "doctors-list";
    }

    // updateDoctorStatus();

    @PostMapping("/{docId}/status")
    public String updateDoctorStatus(@PathVariable("docId") Long docId, @RequestParam("status") EmploymentStatus status){
        docService.updateDoctorStatus(docId, status);       
        return "redirect:/doctors";
    }

    // updateConsultationFee();

    @PostMapping("/{docId}/consultationFee")
    public String updateDoctorConsultationFee(@PathVariable("docId") Long docId, @RequestParam("consultationFee") BigDecimal consultationFee){
        docService.updateConsultationFee(docId, consultationFee);
        return "redirect:/doctors";
    }

    // changeDoctorDepartment();
    @PostMapping("/{docId}/department")
    public String updateDoctorDepartment(@PathVariable("docId") Long docId, @RequestParam("deptId") Long deptId){
        docService.updateDoctorDepartment(docId, deptId);
        return "redirect:/doctors";
    }






}
