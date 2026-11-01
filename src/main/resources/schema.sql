-- Hospital Management & Patient Care System
DROP DATABASE IF EXISTS hospital_db;
CREATE DATABASE hospital_db
    CHARACTER SET utf8mb4
COLLATE utf8mb4_0900_ai_ci;
USE hospital_db;


-- 1. department : Hospital departments (clinical and support).
CREATE TABLE department (
    dept_id INT AUTO_INCREMENT PRIMARY KEY,
    dept_name VARCHAR(60) NOT NULL UNIQUE,
    location VARCHAR(60) NOT NULL,
    phone VARCHAR(15) DEFAULT NULL
) ENGINE = InnoDB;


-- 2. staff : Non-doctor hospital staff (nurses, receptionists, technicians, etc.).
CREATE TABLE doctor(
    doctor_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(40) NOT NULL,
    last_name  VARCHAR(40) NOT NULL,
    gender ENUM('Male', 'Female', 'Other') NOT NULL.
    specialisation VARCHAR(60) NOT NULL UNIQUE,
    dept_id INT NOT NULL,
    consultation_fee DECIMAL(8,2) NOT NULL DEFAULT 500.00,
    join_date DATE NOT NULL,
    status ENUM('Active', 'On Leave', 'Resigned') NOT NULL DEFAULT 'Active',
    CONSTRAINT fk_doctor_dept_id FOREIGN KEY(dept_id) REFERENCES department(dept_id),
    CONSTRAINT chk_doctor_fee CHECK(consultation_fee >= 0)
)ENGINE = InnoDB;


-- 3. staff : Non-doctor hospital staff (nurses, receptionists, technicians, etc.).
CREATE TABLE staff (
    staff_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(40) NOT NULL,
    last_name VARCHAR(40) NOT NULL,
    role ENUM('Nurse','Receptionist','Lab Technician','Pharmacist','Administrator') NOT NULL,
    dept_id INT NOT NULL,
    phone VARCHAR(15) NOT NULL UNIQUE,
    salary DECIMAL(10,2) NOT NULL,
    hire_date DATE NOT NULL,
    CONSTRAINT fk_staff_dept_id FOREIGN KEY (dept_id)
    REFERENCES department(dept_id),
    CONSTRAINT chk_staff_salary CHECK (salary > 0)
) ENGINE = InnoDB;


-- 4. patient : Registered patients
CREATE TABLE patient (
    patient_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(40) NOT NULL,
    last_name VARCHAR(40) NOT NULL,
    ENUM('Male','Female','Other') NOT NULL,
    date_of_birth DATE NOT NULL,
    blood_group ENUM('A+','A-','B+','B-','AB+','AB-','O+','O-') DEFAULT NULL,
    phone VARCHAR(15) NOT NULL UNIQUE,
    email VARCHAR(80) DEFAULT NULL UNIQUE,
    address VARCHAR(150) DEFAULT NULL,
    city VARCHAR(50) NOT NULL,
    registered_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE = InnoDB;


-- 5. insurance_provider : Insurance companies that issue policies.
CREATE TABLE insurance_provider (
    provider_id INT AUTO_INCREMENT PRIMARY KEY,
    provider_name VARCHAR(80) NOT NULL UNIQUE,
    contact_phone VARCHAR(15) DEFAULT NULL,
    email VARCHAR(80) DEFAULT NULL
) ENGINE = InnoDB;


-- 6. patient_insurance : Insurance policies held by patients.
CREATE TABLE patient_insurance (
    policy_id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT NOT NULL,
    provider_id INT NOT NULL,
    policy_number VARCHAR(30) NOT NULL UNIQUE,
    coverage_percent DECIMAL(5,2) NOT NULL,
    max_coverage_amount DECIMAL(12,2) NOT NULL,
    valid_from DATE NOT NULL,
    valid_to DATE NOT NULL,
    CONSTRAINT fk_patient_insurance_patient_id FOREIGN KEY (patient_id)
    REFERENCES patient(patient_id),
    CONSTRAINT fk_patient_insurance_provider_id FOREIGN KEY (provider_id)
    REFERENCES insurance_provider(provider_id),
    CONSTRAINT chk_ins_percent CHECK (coverage_percent BETWEEN 0 AND 100),
    CONSTRAINT chk_ins_max CHECK (max_coverage_amount > 0),
    CONSTRAINT chk_ins_dates CHECK (valid_to > valid_from)
) ENGINE = InnoDB;


-- 7. doctor_availability : Weekly consultation schedule of each doctor.
CREATE TABLE doctor_availability (
    availability_id INT AUTO_INCREMENT PRIMARY KEY,
    doctor_id INT NOT NULL,
    day_of_week ENUM('Mon','Tue','Wed','Thu','Fri','Sat','Sun') NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    slot_minutes INT NOT NULL DEFAULT 30,
    CONSTRAINT fk_doctor_availability_doctor_id FOREIGN KEY (doctor_id)
    REFERENCES doctor(doctor_id) ON DELETE CASCADE,
    CONSTRAINT chk_avail_time CHECK (end_time > start_time),
    CONSTRAINT chk_avail_slot CHECK (slot_minutes > 0),
    CONSTRAINT uq_avail_session UNIQUE (doctor_id, day_of_week, start_time)
) ENGINE = InnoDB;


-- 8. appointment : Patient appointments with doctors.
CREATE TABLE appointment (
    appointment_id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT NOT NULL,
    doctor_id INT NOT NULL,
    appointment_date DATE NOT NULL,
    appointment_time TIME NOT NULL,
    reason VARCHAR(200) DEFAULT NULL,
    status ENUM('Scheduled','Completed','Cancelled','No-Show') NOT NULL DEFAULT 'Scheduled',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    active_slot VARCHAR(40) GENERATED ALWAYS AS (IF(status IN ('Scheduled','Completed'),
    CONCAT(doctor_id,'#',appointment_date,'#',appointment_time), NULL)) STORED,
    CONSTRAINT fk_appointment_patient_id FOREIGN KEY (patient_id)
    REFERENCES patient(patient_id),
    CONSTRAINT fk_appointment_doctor_id FOREIGN KEY (doctor_id)
    REFERENCES doctor(doctor_id),
    CONSTRAINT uq_active_slot UNIQUE (active_slot)
) ENGINE = InnoDB;



-- 9. medical_record : Diagnosis and treatment notes for a consultation or admission.
CREATE TABLE medical_record (
    record_id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT NOT NULL,
    doctor_id INT NOT NULL,
    appointment_id INT DEFAULT NULL UNIQUE,
    visit_date DATE NOT NULL,
    symptoms VARCHAR(255) DEFAULT NULL,
    diagnosis VARCHAR(255) NOT NULL,
    treatment_notes TEXT,
    CONSTRAINT fk_medical_record_patient_id FOREIGN KEY (patient_id)
    REFERENCES patient(patient_id),
    CONSTRAINT fk_medical_record_doctor_id FOREIGN KEY (doctor_id)
    REFERENCES doctor(doctor_id),
    CONSTRAINT fk_medical_record_appointment_id FOREIGN KEY (appointment_id)
    REFERENCES appointment(appointment_id)
) ENGINE = InnoDB;



-- 10. medicine : Pharmacy medicine catalogue with stock.
CREATE TABLE medicine (
    medicine_id INT AUTO_INCREMENT PRIMARY KEY,
    medicine_name VARCHAR(80) NOT NULL UNIQUE,
    manufacturer VARCHAR(80) DEFAULT NULL,
    unit_price DECIMAL(8,2) NOT NULL,
    stock_quantity INT NOT NULL DEFAULT 0,
    CONSTRAINT chk_med_price CHECK (unit_price >= 0),
    CONSTRAINT chk_med_stock CHECK (stock_quantity >= 0)
) ENGINE = InnoDB;



-- 11. prescription : Prescriptions issued against a medical record.
CREATE TABLE prescription (
    prescription_id INT AUTO_INCREMENT PRIMARY KEY,
    record_id INT NOT NULL,
    prescribed_date DATE NOT NULL,
    notes VARCHAR(200) DEFAULT NULL,
    CONSTRAINT fk_prescription_record_id FOREIGN KEY (record_id)
    REFERENCES medical_record(record_id)
) ENGINE = InnoDB;


-- 12. prescription_item : Individual medicines inside a prescription.
CREATE TABLE prescription_item (
    item_id INT AUTO_INCREMENT PRIMARY KEY,
    prescription_id INT NOT NULL,
    medicine_id INT NOT NULL,
    dosage VARCHAR(40) NOT NULL,
    frequency VARCHAR(40) NOT NULL,
    duration_days INT NOT NULL,
    quantity INT NOT NULL,
    CONSTRAINT fk_prescription_item_prescription_id FOREIGN KEY (prescription_id)
    REFERENCES prescription(prescription_id) ON DELETE CASCADE,
    CONSTRAINT fk_prescription_item_medicine_id FOREIGN KEY (medicine_id)
    REFERENCES medicine(medicine_id),
    CONSTRAINT chk_pitem_duration CHECK (duration_days > 0),
    CONSTRAINT chk_pitem_qty CHECK (quantity > 0),
    CONSTRAINT uq_presc_medicine UNIQUE (prescription_id, medicine_id)
) ENGINE = InnoDB;



-- 13. lab_test : Catalogue of laboratory and diagnostic tests.
CREATE TABLE lab_test (
    test_id INT AUTO_INCREMENT PRIMARY KEY,
    test_name VARCHAR(80) NOT NULL UNIQUE,
    normal_range VARCHAR(60) DEFAULT NULL,
    cost DECIMAL(8,2) NOT NULL,
    CONSTRAINT chk_test_cost CHECK (cost >= 0)
) ENGINE = InnoDB;



-- 14. lab_order : Lab tests ordered for a patient visit, with results.
CREATE TABLE lab_order (
    lab_order_id INT AUTO_INCREMENT PRIMARY KEY,
    record_id INT NOT NULL,
    test_id INT NOT NULL,
    ordered_date DATE NOT NULL,
    result_value VARCHAR(60) DEFAULT NULL,
    result_date DATE DEFAULT NULL,
    status ENUM('Ordered','Completed') NOT NULL DEFAULT 'Ordered',
    CONSTRAINT fk_lab_order_record_id FOREIGN KEY (record_id)
    REFERENCES medical_record(record_id),
    CONSTRAINT fk_lab_order_test_id FOREIGN KEY (test_id)
    REFERENCES lab_test(test_id),
    CONSTRAINT chk_lab_dates CHECK (result_date IS NULL OR result_date >= ordered_date)
) ENGINE = InnoDB;




-- 15. ward : Hospital wards.
CREATE TABLE ward (
    ward_id INT AUTO_INCREMENT PRIMARY KEY,
    ward_name VARCHAR(50) NOT NULL UNIQUE,
    ward_type ENUM('General','Private','ICU','Maternity') NOT NULL,
    floor_no INT NOT NULL,
    daily_charge DECIMAL(8,2) NOT NULL,
    CONSTRAINT chk_ward_charge CHECK (daily_charge >= 0),
    CONSTRAINT chk_ward_floor CHECK (floor_no >= 0)
) ENGINE = InnoDB;



-- 16. bed : Beds inside wards.
CREATE TABLE bed (
    bed_id INT AUTO_INCREMENT PRIMARY KEY,
    ward_id INT NOT NULL,
    bed_number VARCHAR(10) NOT NULL,
    status ENUM('Available','Occupied','Maintenance') NOT NULL DEFAULT 'Available',
    CONSTRAINT fk_bed_ward_id FOREIGN KEY (ward_id)
    REFERENCES ward(ward_id),
    CONSTRAINT uq_bed_in_ward UNIQUE (ward_id, bed_number)
) ENGINE = InnoDB;




-- 17. admission : In-patient admissions and discharges.
CREATE TABLE admission (
    admission_id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT NOT NULL,
    doctor_id INT NOT NULL,
    bed_id INT NOT NULL,
    admit_datetime DATETIME NOT NULL,
    discharge_datetime DATETIME DEFAULT NULL,
    reason VARCHAR(200) NOT NULL,
    status ENUM('Admitted','Discharged') NOT NULL DEFAULT 'Admitted',
    discharge_summary TEXT,
    CONSTRAINT fk_admission_patient_id FOREIGN KEY (patient_id)
    REFERENCES patient(patient_id),
    CONSTRAINT fk_admission_doctor_id FOREIGN KEY (doctor_id)
    REFERENCES doctor(doctor_id),
    CONSTRAINT fk_admission_bed_id FOREIGN KEY (bed_id)
    REFERENCES bed(bed_id),
    CONSTRAINT chk_adm_dates CHECK (discharge_datetime IS NULL OR discharge_datetime > admit_datetime),
    CONSTRAINT chk_adm_status CHECK ((status = 'Admitted' AND discharge_datetime IS NULL)
    OR (status = 'Discharged' AND discharge_datetime IS NOT NULL))
) ENGINE = InnoDB;




-- 18. bill : Bills raised for an outpatient appointment or an admission.
CREATE TABLE bill (
    bill_id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT NOT NULL,
    appointment_id INT DEFAULT NULL UNIQUE,
    admission_id INT DEFAULT NULL UNIQUE,
    bill_date DATE NOT NULL,
    total_amount DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    insurance_covered DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    payment_status ENUM('Pending','Partial','Paid') NOT NULL DEFAULT 'Pending',
    CONSTRAINT fk_bill_patient_id FOREIGN KEY (patient_id)
    REFERENCES patient(patient_id),
    CONSTRAINT fk_bill_appointment_id FOREIGN KEY (appointment_id)
    REFERENCES appointment(appointment_id),
    CONSTRAINT fk_bill_admission_id FOREIGN KEY (admission_id)
    REFERENCES admission(admission_id),
    CONSTRAINT chk_bill_amounts CHECK (total_amount >= 0 AND insurance_covered >= 0
    AND insurance_covered <= total_amount),
    CONSTRAINT chk_bill_source CHECK (appointment_id IS NOT NULL OR admission_id IS NOT NULL)
) ENGINE = InnoDB;




-- 19. bill_item : Itemised lines of a bill.
CREATE TABLE bill_item (
    bill_item_id INT AUTO_INCREMENT PRIMARY KEY,
    bill_id INT NOT NULL,
    item_type ENUM('Consultation','Medicine','Lab Test','Room Charge','Procedure') NOT NULL,
    description VARCHAR(120) NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    unit_price DECIMAL(10,2) NOT NULL,
    amount DECIMAL(12,2) GENERATED ALWAYS AS (quantity * unit_price) STORED,
    CONSTRAINT fk_bill_item_bill_id FOREIGN KEY (bill_id)
    REFERENCES bill(bill_id) ON DELETE CASCADE,
    CONSTRAINT chk_bitem_qty CHECK (quantity > 0),
    CONSTRAINT chk_bitem_price CHECK (unit_price >= 0)
) ENGINE = InnoDB;




-- 20. payment : Payments received from patients against bills.
CREATE TABLE payment (
    payment_id INT AUTO_INCREMENT PRIMARY KEY,
    bill_id INT NOT NULL,
    payment_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    amount DECIMAL(12,2) NOT NULL,
    payment_mode ENUM('Cash','Card','UPI','Net Banking') NOT NULL,
    reference_no VARCHAR(40) DEFAULT NULL UNIQUE,
    CONSTRAINT fk_payment_bill_id FOREIGN KEY (bill_id)
    REFERENCES bill(bill_id),
    CONSTRAINT chk_pay_amount CHECK (amount > 0)
) ENGINE = InnoDB;