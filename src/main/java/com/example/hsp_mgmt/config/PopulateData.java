package com.example.hsp_mgmt.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.hsp_mgmt.model.Department;
import com.example.hsp_mgmt.model.Doctor;
import com.example.hsp_mgmt.repository.DepartmentRepository;
import com.example.hsp_mgmt.repository.DoctorRepository;

import jakarta.transaction.Transactional;

@Component 
@Transactional 
public class PopulateData implements CommandLineRunner{
   
    private final DepartmentRepository deptRepo;

    private final DoctorRepository docRepo;

    PopulateData(DepartmentRepository deptRepo, DoctorRepository docRepo){
        this.deptRepo = deptRepo;
        this.docRepo = docRepo;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception{

        //Department must popluate first, hence the first check
        //deptId in Doctor cannot be null
        if(deptRepo.count() > 0 || docRepo.count() > 0){
            System.out.println("Database is already populated!");;
            return;
        }

        System.out.println("Populating database");

        List<Department> depts = DepartmentData.get();
        List<Department> savedDepts = deptRepo.saveAll(depts);
        System.out.println("Successfully saved " + savedDepts.size() + " records in the Department table.");

        List<Doctor> docs = DoctorData.get(savedDepts);
        List<Doctor> savedDocs = docRepo.saveAll(docs);

        System.out.println("Successfully saved " + savedDocs.size() + " records in the Doctor table.");

        System.out.println("Database populated successfully.");

    }

    
}
