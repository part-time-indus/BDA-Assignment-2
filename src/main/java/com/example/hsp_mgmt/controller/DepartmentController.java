package com.example.hsp_mgmt.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import com.example.hsp_mgmt.dto.DepartmentRequestDto;
import com.example.hsp_mgmt.dto.DepartmentResponseDto;
import com.example.hsp_mgmt.model.enums.EmploymentStatus;
import com.example.hsp_mgmt.service.DepartmentService;



@Controller 
@RequestMapping("/departments")
public class DepartmentController {
    
    private final DepartmentService deptService;
    
    public DepartmentController(DepartmentService deptService){
        this.deptService = deptService;
    }

    @GetMapping("/new")
    public String showCreateForm(Model model){
        model.addAttribute("departmentForm", new DepartmentRequestDto());
        return "create-department";
    }

    @PostMapping
    public String createDepartment(@ModelAttribute("departmentForm") DepartmentRequestDto req){
        deptService.createDepartment(req);
        return "redirect:/departments";
    }

    @GetMapping("/edit/{deptId}")
    public String showUpdateForm(@PathVariable("deptId") Long deptId, Model model){
        DepartmentResponseDto currDept = deptService.getDepartmentById(deptId);
        DepartmentRequestDto newDept = new DepartmentRequestDto();
        newDept.setDeptName(currDept.getDeptName());
        newDept.setLocation(currDept.getLocation());
        newDept.setPhone(currDept.getPhone());

        model.addAttribute("departmentForm", newDept);
        model.addAttribute("deptId", deptId);
        return "update-department";

    }

    @PostMapping("/{deptId}")
    public String updateDepartment(@PathVariable("deptId") Long deptId, @ModelAttribute("departmentForm") DepartmentRequestDto req){
        deptService.updateDepartment(deptId, req);
        return "redirect:/departments";

    }

    @GetMapping("/{deptId}")
    public String getDepartmentById(@PathVariable("deptId") Long deptId, Model model){
        DepartmentResponseDto resp = deptService.getDepartmentById(deptId);
        model.addAttribute("department", resp);
        return "department-detail";

    }

    @GetMapping
    public String getAllDepartments(Model model){
        List<DepartmentResponseDto> depts = deptService.getAllDepartments();
        model.addAttribute("departmentList", depts);
        return "departments-list";

    }

    @PostMapping("/delete/{deptId}")
    public String deleteDepartment(@PathVariable("deptId") Long deptId){
        deptService.deleteDepartment(deptId);
        return "redirect:/departments";
    }


    @GetMapping("/name")
    public String getDepartmentByName(@RequestParam("deptName") String deptName, Model model){
        DepartmentResponseDto resp = deptService.getDepartmentByName(deptName);
        model.addAttribute("department", resp);
        return "department-detail";

    }

    @GetMapping("/location")
    public String getDepartmentByLocation(@RequestParam("location") String location, Model model){
        List<DepartmentResponseDto> res = deptService.getDepartmentsByLocation(location);
        model.addAttribute("departmentList", res);
        return "departments-list";

    }


    @GetMapping("/doctors/status")
    public String getDepartmentsWithDoctorsByStatus(@RequestParam("status") EmploymentStatus status, Model model){
        List<DepartmentResponseDto> resp = deptService.getDepartmentsWithDoctorsByStatus(status);
        model.addAttribute("departmentList", resp);
        return "departments-list";
    }


    @GetMapping("/doctors/empty")
    public String getDepartmentsWithoutDoctors(Model model){
        List<DepartmentResponseDto> resp = deptService.getDepartmentsWithoutDoctors();
        model.addAttribute("departmentList", resp);
        return "departments-list";
    }


    @GetMapping("/doctors/specialisation")
    public String getDepartmentsBySpecialisation(@RequestParam("specialisation") String specialisation, Model model){
        List<DepartmentResponseDto> resp = deptService.getDepartmentsBySpecialisation(specialisation);
        model.addAttribute("departmentList", resp);
        return "departments-list";
    }


    @PostMapping("/{deptId}/location")
    public String updateDepartmentLocation(@PathVariable("deptId") Long deptId, @RequestParam("location") String location){
        deptService.updateDepartmentLocation(deptId, location);
        return "redirect:/departments/" + deptId;
    }

    @PostMapping("/{deptId}/phone")
    public String updateDepartmentPhone(@PathVariable("deptId") Long deptId, @RequestParam("phone") String phone){
        deptService.updateDepartmentPhone(deptId, phone);
        return "redirect:/departments/" + deptId;
    }


}
