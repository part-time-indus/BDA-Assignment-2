package com.example.hsp_mgmt.model;

import java.time.LocalTime;

import com.example.hsp_mgmt.model.enums.Day;

import jakarta.persistence.CheckConstraint;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;

// CREATE TABLE doctor_availability (
//     availability_id INT AUTO_INCREMENT PRIMARY KEY,
//     doctor_id INT NOT NULL,
//     day_of_week ENUM('Mon','Tue','Wed','Thu','Fri','Sat','Sun') NOT NULL,
//     start_time TIME NOT NULL,
//     end_time TIME NOT NULL,
//     slot_minutes INT NOT NULL DEFAULT 30,
//     CONSTRAINT fk_doctor_availability_doctor_id FOREIGN KEY (doctor_id)
//     REFERENCES doctor(doctor_id) ON DELETE CASCADE,
//     CONSTRAINT chk_avail_time CHECK (end_time > start_time),
//     CONSTRAINT chk_avail_slot CHECK (slot_minutes > 0),
//     CONSTRAINT uq_avail_session UNIQUE (doctor_id, day_of_week, start_time)
// ) ENGINE = InnoDB;


@Entity 
@Table(name="doctor_availability", options="Engine=InnoDB", 
       uniqueConstraints=@UniqueConstraint(name="uq_avail_session", columnNames={"doctor_id","day_of_week", "start_time"}),
       check={
        @CheckConstraint(name="chk_avail_time", constraint="end_time > start_time"),
        @CheckConstraint(name="chk_avail_slot", constraint="slot_minutes > 0"),
       })
public class DoctorAvailability {

    @Column(name="availability_id")
    @Id 
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long availabilityId;

    @ManyToOne
    @JoinColumn(name="doctor_id", nullable=false, foreignKey=@ForeignKey(name="fk_doctor_availability_doctor_id"))
    @NotNull 
    private Doctor doctor;

    @Enumerated(EnumType.STRING)
    @Column(name="day_of_week", nullable=false)
    @NotNull 
    private Day dayOfWeek;

    @Column(name="start_time", nullable=false)
    @NotNull 
    private LocalTime startTime;

    @Column(name="end_time", nullable=false)
    @NotNull
    private LocalTime endTime;

    @Column(name="slot_minutes", nullable=false)
    @NotNull
    private int slotMinutes = 30;


    //----------------------------------Constructor--------------------------------------


    public DoctorAvailability(@NotNull Doctor doctor, @NotNull Day dayOfWeek, @NotNull LocalTime startTime,
            @NotNull LocalTime endTime, @NotNull int slotMinutes) {
        this.doctor = doctor;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.slotMinutes = slotMinutes;
    }


    public DoctorAvailability() {
    }



    //----------------------------------Getters & Setters--------------------------------


    public Long getAvailabilityId() {
        return availabilityId;
    }


    public Doctor getDoctor() {
        return doctor;
    }


    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }


    public Day getDayOfWeek() {
        return dayOfWeek;
    }


    public void setDayOfWeek(Day dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }


    public LocalTime getStartTime() {
        return startTime;
    }


    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }


    public LocalTime getEndTime() {
        return endTime;
    }


    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }


    public int getSlotMinutes() {
        return slotMinutes;
    }


    public void setSlotMinutes(int slotMinutes) {
        this.slotMinutes = slotMinutes;
    }

    



    
    
}

