package com.example.hsp_mgmt.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.hsp_mgmt.dto.PatientRequestDto;
import com.example.hsp_mgmt.dto.PatientResponseDto;
import com.example.hsp_mgmt.model.Patient;
import com.example.hsp_mgmt.repository.PatientRepository;



@Service 
public class PatientService {
    private final PatientRepository patientRepository;

    public PatientService(com.example.hsp_mgmt.repository.PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Transactional 
    public PatientResponseDto registerPatient(PatientRequestDto request){
        Patient p = new Patient();
        if(patientRepository.findByPhone(request.getPhone()).isPresent()){
            throw new RuntimeException("A patient with phone number " + request.getPhone() + " already exists");
        }

        p.setFirstName(request.getFirstName());
        p.setLastName(request.getLastName());
        p.setGender(request.getGender());
        p.setbGroup(request.getbGroup());
        p.setEmail(request.getEmail());
        p.setPhone(request.getPhone());
        p.setAddress(request.getAddress());
        p.setCity(request.getCity());
        p.setDateOfBirth(request.getDateOfBirth());

        return mapToDto(p);
    }

    @Transactional(readOnly = true)
    public PatientResponseDto getPatientById(Long id){
        Patient p = patientRepository.findById(id)
                              .orElseThrow(() -> new RuntimeException("Patient no found with ID: " + id));
        return mapToDto(p);
    }

    private PatientResponseDto mapToDto(Patient p){
        PatientResponseDto  dto = new PatientResponseDto();
         dto.setPatientId(p.getPatientId());
         dto.setFirstName(p.getFirstName());
         dto.setLastName(p.getLastName());
         dto.setGender(p.getGender());
         dto.setbGroup(p.getbGroup());
         dto.setPhone(p.getPhone());
         dto.setEmail(p.getEmail());
         dto.setAddress(p.getAddress());
         dto.setCity(p.getCity());
         dto.setDateOfBirth(p.getDateOfBirth());
         dto.setRegisteredAt(p.getRegisteredAt());

        return  dto;

    }

    
}
