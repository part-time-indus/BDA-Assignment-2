package com.example.hsp_mgmt.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.example.hsp_mgmt.model.enums.AppointmentStatus;

public class AppointmentResponseDto {
    
    private Long appointmentId;
    private Long patientId;
    private String patientFullName;
    private Long doctorId;
    private String doctorFullName;
    private String departmentName;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private String reason;
    private AppointmentStatus apptStatus;
    private String activeSlot;
    private LocalDateTime ceatedAt;
    
    
    
    public AppointmentResponseDto() {
    }



    
    public Long getAppointmentId() {
        return appointmentId;
    }



    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }



    public Long getPatientId() {
        return patientId;
    }



    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }



    public String getPatientFullName() {
        return patientFullName;
    }



    public void setPatientFullName(String patientFullName) {
        this.patientFullName = patientFullName;
    }



    public Long getDoctorId() {
        return doctorId;
    }



    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }



    public String getDoctorFullName() {
        return doctorFullName;
    }



    public void setDoctorFullName(String doctorFullName) {
        this.doctorFullName = doctorFullName;
    }



    public String getDepartmentName() {
        return departmentName;
    }



    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }



    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }



    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }



    public LocalTime getAppointmentTime() {
        return appointmentTime;
    }



    public void setAppointmentTime(LocalTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }



    public String getReason() {
        return reason;
    }



    public void setReason(String reason) {
        this.reason = reason;
    }



    public AppointmentStatus getApptStatus() {
        return apptStatus;
    }



    public void setApptStatus(AppointmentStatus apptStatus) {
        this.apptStatus = apptStatus;
    }



    public String getActiveSlot() {
        return activeSlot;
    }



    public void setActiveSlot(String activeSlot) {
        this.activeSlot = activeSlot;
    }



    public LocalDateTime getCeatedAt() {
        return ceatedAt;
    }



    public void setCeatedAt(LocalDateTime ceatedAt) {
        this.ceatedAt = ceatedAt;
    }
    


    

    

    
}
