package com.example.hsp_mgmt.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.hsp_mgmt.dto.DepartmentRequestDto;
import com.example.hsp_mgmt.dto.DepartmentResponseDto;
import com.example.hsp_mgmt.model.Department;
import com.example.hsp_mgmt.model.enums.EmploymentStatus;
import com.example.hsp_mgmt.repository.DepartmentRepository;


@Service 
public class DepartmentServiceImpl implements DepartmentService{

    private final DepartmentRepository deptRepo;

    public DepartmentServiceImpl(DepartmentRepository deptRepo){
        this.deptRepo = deptRepo;
    }


    private DepartmentResponseDto toDto(Department dept){
        DepartmentResponseDto resp = new DepartmentResponseDto();
        resp.setDeptId(dept.getDeptId());
        resp.setDeptName(dept.getDeptName());
        resp.setLocation(dept.getLocation());
        resp.setPhone(dept.getPhone());
        resp.setDoctorCount(dept.getDoctors().size());
        return resp;
    }
    

    @Override 
    public DepartmentResponseDto createDepartment(DepartmentRequestDto request){
        Department dept = new Department();
        dept.setDeptName(request.getDeptName());
        dept.setLocation(request.getLocation());
        dept.setPhone(request.getPhone());

        Department savedDept = deptRepo.save(dept);
        return toDto(savedDept);
    }

    @Override 
    public DepartmentResponseDto updateDepartment(Long deptId, DepartmentRequestDto request){
        Department dept = deptRepo.findById(deptId).orElseThrow(() -> new RuntimeException("Department not found"));
        dept.setDeptName(request.getDeptName());
        dept.setLocation(request.getLocation());
        dept.setPhone(request.getPhone());
        Department savedDept = deptRepo.save(dept);
        return toDto(savedDept);
    }

    @Override 
    public DepartmentResponseDto getDepartmentById(Long deptId){
        Department dept = deptRepo.findById(deptId).
                          orElseThrow(() -> new RuntimeException("Department not found"));
        return toDto(dept);
    }

    @Override 
    public List<DepartmentResponseDto> getAllDepartments(){
        List<Department> depts = deptRepo.findAll();
        return depts.stream().map((dept) -> toDto(dept)).toList();
    }

    @Override 
    public void deleteDepartment(Long deptId){
        Department dept = deptRepo.findById(deptId).
                          orElseThrow(() -> new RuntimeException("Department not found"));
        deptRepo.delete(dept);
    }


    // Search operations

    @Override 
    public DepartmentResponseDto getDepartmentByName(String deptName){
        Department dept = deptRepo.findByDeptNameIgnoreCase(deptName).
                          orElseThrow(() -> new RuntimeException("Department " + deptName + " not found" ));
        return toDto(dept);
    }

    @Override 
    public List<DepartmentResponseDto> getDepartmentsByLocation(String location){
        List<Department> depts = deptRepo.findByLocationContainingIgnoreCase(location);
        return depts.stream().map((dept) -> toDto(dept)).toList();
    }


    // Department and doctor operations

    @Override 
    public List<DepartmentResponseDto> getDepartmentsWithDoctorsByStatus(EmploymentStatus status){
        List<Department> depts = deptRepo.findDepartmentsWithDoctorStatus(status);
        return depts.stream().map((dept) -> toDto(dept)).toList();
    }

    @Override 
    public List<DepartmentResponseDto> getDepartmentsWithoutDoctors(){
        List<Department> depts = deptRepo.findDepartmentsWithoutDoctors();
        return depts.stream().map((dept) -> toDto(dept)).toList();
    }

    @Override 
    public List<DepartmentResponseDto> getDepartmentsBySpecialisation(String specialisation){
        List<Department> depts = deptRepo.findDepartmentsBySpecialisation(specialisation);
        return depts.stream().map((dept) -> toDto(dept)).toList();
    }


    // Update operations

    @Override 
    public DepartmentResponseDto updateDepartmentLocation(Long deptId, String location){
        Department dept = deptRepo.findById(deptId).
                          orElseThrow(() -> new RuntimeException("Department not found"));

        dept.setLocation(location);
        Department deptSaved = deptRepo.save(dept);
        return toDto(deptSaved);

    }

    @Override 
    public DepartmentResponseDto updateDepartmentPhone(Long deptId, String phone){
        Department dept = deptRepo.findById(deptId).
                          orElseThrow(() -> new RuntimeException("Department not found"));
        dept.setPhone(phone);
        Department deptSaved = deptRepo.save(dept);
        return toDto(deptSaved);

    }
    
}
