package com.example.hsp_mgmt.config;

import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDate;

import com.example.hsp_mgmt.model.Department;
import com.example.hsp_mgmt.model.Doctor;
import com.example.hsp_mgmt.model.enums.DepartmentType;
import com.example.hsp_mgmt.model.enums.EmploymentStatus;
import com.example.hsp_mgmt.model.enums.Gender;

// -- doctor
// INSERT INTO doctor (doctor_id, first_name, last_name, gender, specialization, phone, email,
// dept_id, consultation_fee, join_date, status) VALUES
// (1, 'Anil', 'Sharma', 'Male', 'Cardiologist', '9811000001', 'anil.sharma@cityhospital.example', 1, 800.00, '2015-06-01', 'Active'),
// (2, 'Meera', 'Kapoor', 'Female', 'Cardiologist', '9811000002', 'meera.kapoor@cityhospital.example', 1, 900.00, '2018-03-15', 'Active'),
// (3, 'Rajesh', 'Verma', 'Male', 'Orthopedic Surgeon', '9811000003', 'rajesh.verma@cityhospital.example', 2, 700.00, '2012-01-10', 'Active'),
// (4, 'Sunita', 'Rao', 'Female', 'Pediatrician', '9811000004', 'sunita.rao@cityhospital.example', 3, 500.00, '2019-07-20', 'Active'),
// (5, 'Vikram', 'Singh', 'Male', 'General Physician', '9811000005', 'vikram.singh@cityhospital.example', 4, 400.00, '2016-09-05', 'Active'),
// (6, 'Neha', 'Gupta', 'Female', 'Neurologist', '9811000006', 'neha.gupta@cityhospital.example', 5, 1000.00, '2020-02-11', 'Active'),
// (7, 'Arjun', 'Mehta', 'Male', 'General Physician', '9811000007', 'arjun.mehta@cityhospital.example', 4, 450.00, '2021-04-01', 'Active'),
// (8, 'Kavita', 'Nair', 'Female', 'Pediatrician', '9811000008', 'kavita.nair@cityhospital.example', 3, 550.00, '2022-08-16', 'On Leave');
// (9, 'Suraj', 'Kumar', 'Male', 'Oncologist', '9824343245', 'suraj.kumar@cityhospital.com', '6', 1050.0, '2023-01-10', 'Active',)
// (10, 'Parneet', 'Kaur', 'Male', 'Dermatologist', '9855543245', 'parneet.kaur@cityhospital.com', '7', 650.0, '2023-11-18', 'Active',)



public class DoctorData{
    public static List<Doctor> get(List<Department> depts){
        return List.of(
            new Doctor("Anil", "Sharma", Gender.MALE, "9811000001", 
                        "anil.sharma@cityhospital.example", "Cardiologist", 
                        DoctorData.deptByName(DepartmentType.CARDIOLOGY.getDeptName(), depts), 
                        new BigDecimal(800.00), LocalDate.parse("2015-06-01"), EmploymentStatus.ACTIVE),

            new Doctor("Meera", "Kapoor", Gender.FEMALE, 
                        "9811000002", "meera.kapoor@cityhospital.example", "Pathologist", 
                        DoctorData.deptByName(DepartmentType.PATHOLOGY.getDeptName(), depts), 
                        new BigDecimal(900.0), LocalDate.parse("2018-03-15"), EmploymentStatus.ACTIVE),

            new Doctor("Rajesh", "Verma", Gender.MALE, "9811000003", 
                        "rajesh.verma@cityhospital.example", "Orthopedic Surgeon",
                        DoctorData.deptByName(DepartmentType.ORTHOPEDICS.getDeptName(), depts), 
                        new BigDecimal(700.0), LocalDate.parse("2012-01-10"), EmploymentStatus.ACTIVE
                        ),
            new Doctor("Sunita", "Rao", Gender.FEMALE, 
                        "9811000004", "sunita.rao@cityhospital.example", "Pediatrician",
                        DoctorData.deptByName(DepartmentType.PEDIATRICS.getDeptName(), depts), 
                        new BigDecimal(500.0), LocalDate.parse("2019-07-20"), EmploymentStatus.ACTIVE),

            new Doctor("Vikram", "Singh", Gender.MALE, 
                        "9811000005", "vikram.singh@cityhospital.example", "General Physician",
                        DoctorData.deptByName(DepartmentType.GENERAL_MEDICINE.getDeptName(), depts), 
                        new BigDecimal(400.0) ,LocalDate.parse("2020-02-11"), EmploymentStatus.ACTIVE),

            new Doctor("Neha", "Gupta", Gender.FEMALE, "9811000006", 
                        "neha.gupta@cityhospital.example", "Neurologist", 
                        DoctorData.deptByName(DepartmentType.NEUROLOGY.getDeptName(), depts), 
                        new BigDecimal(1000.0), LocalDate.parse("2020-02-11"), EmploymentStatus.ACTIVE),
                        
            new Doctor("Arjun", "Mehta", Gender.MALE, "9811000007", "arjun.mehta@cityhospital.example", 
                        "General Physician", DoctorData.deptByName(DepartmentType.GENERAL_MEDICINE.getDeptName(), depts),
                        new BigDecimal(450.0), LocalDate.parse("2021-04-01"), EmploymentStatus.ACTIVE),
                        
            new Doctor("Kavita", "Nair", Gender.FEMALE, "9811000008", "kavita.nair@cityhospital.example",
                        "Urologist", DoctorData.deptByName(DepartmentType.UROLOGY.getDeptName(), depts), new BigDecimal(650.0),
                        LocalDate.parse("2022-08-16"), EmploymentStatus.ACTIVE),

            new Doctor("Suraj", "Kumar", Gender.MALE, "9824343245", "suraj.kumar@cityhospital.com",
                        "Oncologist", DoctorData.deptByName(DepartmentType.ONCOLOGY.getDeptName(), depts), new BigDecimal(1050.0),
                        LocalDate.parse("2023-01-10"), EmploymentStatus.ACTIVE),

            new Doctor("Parneet", "Kaur", Gender.FEMALE, "9855543245", "parneet.kaur@cityhospital.com",
                        "Dermatologist", DoctorData.deptByName(DepartmentType.DERMATOLOGY.getDeptName(), depts),
                        new BigDecimal("600.0"), LocalDate.parse("2023-11-18"), EmploymentStatus.ACTIVE
            )

        );
    }

    public static Department deptByName(String deptName, List<Department> depts){
        for (Department dept : depts) {
            if(dept.getDeptName() != null && dept.getDeptName().toLowerCase().equals(deptName.toLowerCase()))
                return dept;
        }
        return null;

    }
    

}