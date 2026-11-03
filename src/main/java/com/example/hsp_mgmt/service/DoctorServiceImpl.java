package com.example.hsp_mgmt.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.hsp_mgmt.dto.DoctorRequestDto;
import com.example.hsp_mgmt.dto.DoctorResponseDto;
import com.example.hsp_mgmt.model.Department;
import com.example.hsp_mgmt.model.Doctor;
import com.example.hsp_mgmt.model.enums.DepartmentType;
import com.example.hsp_mgmt.model.enums.EmploymentStatus;
import com.example.hsp_mgmt.repository.DepartmentRepository;
import com.example.hsp_mgmt.repository.DoctorRepository;


@Service 
public class DoctorServiceImpl implements DoctorService {
    
    // CRUD operations
    private final DoctorRepository docRepo;
    private final DepartmentRepository depRepo;

    public DoctorServiceImpl(DoctorRepository docRepo, DepartmentRepository depRepo){
        this.docRepo = docRepo;
        this.depRepo = depRepo;
    }

    private DoctorResponseDto toDto(Doctor doc){
        DoctorResponseDto resp = new DoctorResponseDto();
        resp.setDoctorId(doc.getDoctorId());
        resp.setDeptId(doc.getDepartment().getDeptId());
        resp.setDeptLocation(doc.getDepartment().getLocation());
        resp.setDeptName(doc.getDepartment().getDeptName());
        resp.setConsultationFee(doc.getConsultationFee());
        resp.setEmail(doc.getEmail());
        resp.setFirstName(doc.getFirstName());
        resp.setLastName(doc.getLastName());
        resp.setJoinDate(doc.getJoinDate());
        resp.setPhone(doc.getPhone());
        resp.setStatus(doc.getStatus());
        resp.setSecialization(doc.getSpecialisation());
        return resp;

    }
    
    //CRUD operations
    
    @Override
    public DoctorResponseDto addDoctor(DoctorRequestDto request){
        Doctor doc = new Doctor();
        doc.setConsultationFee(request.getConsultationFee());
        doc.setDepartment(depRepo.findById(request.getDepartmentId()).
                          orElseThrow(() -> new RuntimeException("Department Id not found")));
        doc.setEmail(request.getEmail());
        doc.setFirstName(request.getFirstName());
        doc.setLastName(request.getLastName());
        doc.setGender(doc.getGender());
        doc.setJoinDate(doc.getJoinDate());
        doc.setPhone(doc.getPhone());
        doc.setSpecialisation(doc.getSpecialisation());
        doc.setStatus(doc.getStatus());

        Doctor docSaved = docRepo.save(doc);
        return toDto(docSaved);
    }

    @Override
    public DoctorResponseDto updateDoctor(Long doctorId, DoctorRequestDto request){
        Doctor doc = docRepo.findById(doctorId).
                     orElseThrow(() -> new RuntimeException("Doctor not found"));
        doc.setConsultationFee(request.getConsultationFee());
        doc.setDepartment(depRepo.findById(request.getDepartmentId()).orElseThrow(() -> new RuntimeException("Department not found")));
        doc.setEmail(request.getEmail());
        doc.setFirstName(request.getFirstName());
        doc.setLastName(request.getLastName());
        doc.setJoinDate(request.getJoinDate());
        doc.setGender(request.getGender());
        doc.setPhone(request.getPhone());
        doc.setSpecialisation(request.getSplz());
        doc.setStatus(request.getStatus());

        Doctor docSaved = docRepo.save(doc);
        return toDto(docSaved);
    }

    @Override
    public DoctorResponseDto getDoctorById(Long doctorId){
        Doctor doc = docRepo.findById(doctorId).orElseThrow(() -> new RuntimeException("Doctor not found"));
        return toDto(doc);
    }

    @Override
    public List<DoctorResponseDto> getAllDoctors(){
        List<Doctor> docs = docRepo.findAll();
        return docs.stream().map((doc) -> toDto(doc)).toList();
    }

    @Override
    public void deleteDoctor(Long doctorId){
        Doctor doc = docRepo.findById(doctorId).orElseThrow(() -> new RuntimeException("Doctor not found"));
        docRepo.delete(doc);

    }


    //Search opeartions

    @Override
    public DoctorResponseDto getDoctorByPhone(String phone){
        Doctor doc = docRepo.findByPhone(phone).orElseThrow(() -> new RuntimeException("Doctor with phone " + phone + " not found"));
        return toDto(doc);
    }

    @Override
    public DoctorResponseDto getDoctorByEmail(String email){
        Doctor doc = docRepo.findByEmailIgnoreCase(email).orElseThrow(() -> new RuntimeException("Doctor with email " + email + " not found"));
        return toDto(doc);
    }


    @Override
    public List<DoctorResponseDto> getDoctorsBySpecialisation(String specialisation){
        List<Doctor> docs = docRepo.findBySpecialisationIgnoreCase(specialisation);
        return docs.stream().map((doc) -> toDto(doc)).toList();
    }


    @Override
    public List<DoctorResponseDto> getDoctorsByDepartment(Long deptId){
        Department dept = depRepo.findById(deptId).orElseThrow(() -> new RuntimeException("Department not found"));
        DepartmentType deptType = DepartmentType.fromDeptName(dept.getDeptName());
        return getDoctorsByDepartmentName(deptType);
    }

    @Override
    public List<DoctorResponseDto> getDoctorsByDepartmentName(DepartmentType dept){
        List<Doctor> docs = docRepo.findDoctorsByDepartmentName(dept.getDeptName());
        return docs.stream().map((doc) -> toDto(doc)).toList();
    }

    @Override
    public List<DoctorResponseDto> getDoctorsByDepartmentAndStatus(Long deptId, EmploymentStatus status){
        Department dep = depRepo.findById(deptId).orElseThrow(() -> new RuntimeException("Department not found"));
        List<Doctor> docs = docRepo.findDeptId(dep.getDeptId());
        docs = docs.stream().filter((doc) -> doc.getStatus().equals(status)).toList();
        return docs.stream().map((doc) -> toDto(doc)).toList();

    }


    //Update operations

    @Override
    public DoctorResponseDto updateDoctorStatus(Long doctorId, EmploymentStatus status){
        Doctor doc = docRepo.findById(doctorId).orElseThrow(() -> new RuntimeException("Doctor not found"));
        doc.setStatus(status);
        Doctor docSaved = docRepo.save(doc);
        return toDto(docSaved);
    }

    @Override
    public DoctorResponseDto updateConsultationFee(Long doctorId, BigDecimal fee){
        Doctor doc = docRepo.findById(doctorId).orElseThrow(() -> new RuntimeException("Doctor not found"));
        doc.setConsultationFee(fee);
        Doctor docSaved = docRepo.save(doc);
        return toDto(docSaved);


    }


    @Override
    public DoctorResponseDto changeDoctorDepartment(Long doctorId, Long newDeptId){
        Doctor doc = docRepo.findById(doctorId).orElseThrow(() -> new RuntimeException("Doctor not found"));
        Department dep = depRepo.findById(newDeptId).orElseThrow(() -> new RuntimeException("Department not found"));
        doc.setDepartment(dep);
        Doctor docSaved = docRepo.save(doc);
        return toDto(docSaved);


    }

}
