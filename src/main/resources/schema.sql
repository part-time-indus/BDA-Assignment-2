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


-- Doctor 
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
