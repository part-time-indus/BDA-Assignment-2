package com.example.hsp_mgmt.model.enums;


public enum DepartmentType {
        CARDIOLOGY("Cardiology"),
        ORTHOPEDICS("Orthopedics"),
        PEDIATRICS("Pediatrics"),
        GENERAL_MEDICINE("General Medicine"),
        NEUROLOGY("Neurology"),
        PATHOLOGY("Pathology"),
        ONCOLOGY("Oncology"),
        DERMATOLOGY("Dermatoloy"), 
        RADIOLOGY("Radiology"), 
        UROLOGY("Urology");

        private String deptName;
        private DepartmentType(String deptName) {
            this.deptName = deptName;
        }
        public String getDeptName() {
            return deptName;
        }     

        
}
