package com.example.hsp_mgmt.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.hsp_mgmt.dto.AppointmentRequestDto;
import com.example.hsp_mgmt.dto.AppointmentResponseDto;
import com.example.hsp_mgmt.model.Appointment;
import com.example.hsp_mgmt.model.Doctor;
import com.example.hsp_mgmt.model.Patient;
import com.example.hsp_mgmt.model.enums.AppointmentStatus;
import com.example.hsp_mgmt.repository.AppointmentRepository;
import com.example.hsp_mgmt.repository.DoctorRepository;
import com.example.hsp_mgmt.repository.PatientRepository;


@Service 
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    public AppointmentService(AppointmentRepository appointmentRepository, 
                              PatientRepository patientRepository,
                              DoctorRepository doctorRepository) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    @Transactional 
    public AppointmentResponseDto bookAppointment(AppointmentRequestDto request){
        Patient patient = patientRepository.findById(request.getPatientId())
                          .orElseThrow(() -> new RuntimeException("Patient not found with ID:" + request.getPatientId()));
        
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                        .orElseThrow(() -> new RuntimeException("Doctor not found with ID: " + request.getDoctorId()));
        
        Appointment appt = new Appointment();
        appt.setDoctor(doctor);
        appt.setPatient(patient);
        appt.setAppointment_date(request.getAppointmentDate());
        appt.setAppointment_time(request.getAppointmentTime());
        appt.setReason(request.getReason());
        appt.setApptStatus(AppointmentStatus.SCHEDULED);

        Appointment savedAppt = appointmentRepository.save(appt);
        return mapToDto(savedAppt);

    }

    @Transactional(readOnly = true)
    public List<AppointmentResponseDto> getAppointmentsByDoctor(Long doctorId){
        List<Appointment> appts = appointmentRepository.findByDoctorId(doctorId);
        return appts.stream().map(this::mapToDto).toList();       
    }

    private AppointmentResponseDto mapToDto(Appointment appt){
        AppointmentResponseDto respDto = new AppointmentResponseDto();

        respDto.setAppointmentId(appt.getAppointmentId());
        respDto.setPatientId(appt.getPatient().getPatientId());
        respDto.setDoctorId(appt.getDoctor().getDoctorId());
        respDto.setPatientFullName(appt.getPatient().getFirstName() + " " + appt.getPatient().getLastName());
        respDto.setDoctorFullName(appt.getDoctor().getFirstName() + " " + appt.getDoctor().getLastName());
        respDto.setDepartmentName(appt.getDoctor().getDepartment().getDeptName());
        respDto.setAppointmentDate(appt.getAppointment_date());
        respDto.setAppointmentTime(appt.getAppointment_time());
        respDto.setApptStatus(appt.getApptStatus());
        respDto.setCeatedAt(appt.getCreatedAt());
        respDto.setActiveSlot(appt.getActiveSlot());

        return respDto;
    }


    
}
