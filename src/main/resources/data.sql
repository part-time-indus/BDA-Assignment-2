-- 1. department
INSERT INTO department (dept_id, dept_name, location, phone) VALUES
(1, 'Cardiology','Block A, Floor 2', '0112000101'),
(2, 'Orthopedics','Block B, Floor 1', '0112000102'),
(3, 'Pediatrics','Block A, Floor 1', '0112000103'),
(4, 'General Medicine', 'Block C, Floor 1', '0112000104'),
(5, 'Neurology','Block A, Floor 3', '0112000105'),
(6, 'Pathology','Block D, Ground', '0112000106');





-- 2. doctor
INSERT INTO doctor (doctor_id, first_name, last_name, gender, specialization, phone, email,
dept_id, consultation_fee, join_date, status) VALUES
(1, 'Anil', 'Sharma', 'Male', 'Cardiologist', '9811000001', 'anil.sharma@cityhospital.example', 1, 800.00, '2015-06-01', 'Active'),
(2, 'Meera', 'Kapoor', 'Female', 'Cardiologist', '9811000002', 'meera.kapoor@cityhospital.example', 1, 900.00, '2018-03-15', 'Active'),
(3, 'Rajesh', 'Verma', 'Male', 'Orthopedic Surgeon', '9811000003', 'rajesh.verma@cityhospital.example', 2, 700.00, '2012-01-10', 'Active'),
(4, 'Sunita', 'Rao', 'Female', 'Pediatrician', '9811000004', 'sunita.rao@cityhospital.example', 3, 500.00, '2019-07-20', 'Active'),
(5, 'Vikram', 'Singh', 'Male', 'General Physician', '9811000005', 'vikram.singh@cityhospital.example', 4, 400.00, '2016-09-05', 'Active'),
(6, 'Neha', 'Gupta', 'Female', 'Neurologist', '9811000006', 'neha.gupta@cityhospital.example', 5, 1000.00, '2020-02-11', 'Active'),
(7, 'Arjun', 'Mehta', 'Male', 'General Physician', '9811000007', 'arjun.mehta@cityhospital.example', 4, 450.00, '2021-04-01', 'Active'),
(8, 'Kavita', 'Nair', 'Female', 'Pediatrician', '9811000008', 'kavita.nair@cityhospital.example', 3, 550.00, '2022-08-16', 'On Leave');




-- 3. staff
INSERT INTO staff (staff_id, first_name, last_name, role, dept_id, phone, salary, hire_date) VALUES
(1, 'Pooja','Malhotra', 'Nurse',1, '9822000001', 32000.00, '2017-05-10'),
(2, 'Ravi','Kumar','Receptionist',4, '9822000002', 24000.00, '2019-01-14'),
(3, 'Anjali','Das','Lab Technician', 6, '9822000003', 30000.00, '2018-11-03'),
(4, 'Sandeep', 'Bhatia','Pharmacist',4, '9822000004', 35000.00, '2016-02-20'),
(5, 'Kiran','Joshi','Nurse',3, '9822000005', 31000.00, '2020-06-25'),
(6, 'Harpreet', 'Gill','Administrator', 4, '9822000006', 45000.00, '2014-08-01');



--4. patient
INSERT INTO patient (patient_id, first_name, last_name, gender, date_of_birth, blood_group,
phone, email, address, city) VALUES
(1, 'Rohan', 'Mehra', 'Male', '1985-03-12', 'B+', '9900000001', 'rohan.mehra@mail.example', '12, Model Town', 'Chandigarh'),
(2, 'Priya', 'Sethi', 'Female', '1992-07-25', 'O+', '9900000002', 'priya.sethi@mail.example', '88, Lawrence Road', 'Amritsar'),
(3, 'Amit', 'Khanna', 'Male', '1968-11-02', 'A+', '9900000003', 'amit.khanna@mail.example', '5, Civil Lines', 'Jalandhar'),
(4, 'Simran', 'Kaur', 'Female', '1999-01-18', 'AB+', '9900000004', 'simran.kaur@mail.example', '21, Leela Bhawan', 'Patiala'),
(5, 'Aarav', 'Jain', 'Male', '2018-05-09', 'O-', '9900000005', NULL, '14, Sector 22', 'Chandigarh'),
(6, 'Deepika', 'Arora', 'Female', '1975-09-30', 'B-', '9900000006', 'deepika.arora@mail.example', '9, Rajouri Garden', 'Delhi'),
(7, 'Harish', 'Bansal', 'Male', '1958-04-14', 'A-', '9900000007', NULL, '33, Ranjit Avenue', 'Amritsar'),
(8, 'Tanya', 'Oberoi', 'Female', '2015-12-01', 'B+', '9900000008', NULL, '60, Phase 5', 'Mohali'),
(9, 'Manoj', 'Tiwari', 'Male', '1980-06-21', 'O+', '9900000009', 'manoj.tiwari@mail.example', '17, Adarsh Nagar', 'Jalandhar'),
(10, 'Ritu', 'Chawla', 'Female', '1990-02-27', 'A+', '9900000010', 'ritu.chawla@mail.example', '3, Urban Estate', 'Patiala'),
(11, 'Gurpreet', 'Singh', 'Male', '1972-08-08', 'AB-', '9900000011', 'gurpreet.singh@mail.example','44, Sector 35', 'Chandigarh'),
(12, 'Nisha', 'Pandey', 'Female', '2001-10-16', 'O+', '9900000012', 'nisha.pandey@mail.example', '27, Saket', 'Delhi');





-- 5. insurance_provider
INSERT INTO insurance_provider (provider_id, provider_name, contact_phone, email) VALUES
(1, 'SecureLife Health Insurance', '1800100001', 'claims@securelife.example'),
(2, 'MediAssure General Insurance', '1800100002', 'claims@mediassure.example'),
(3, 'National Care Insurance','1800100003', 'claims@nationalcare.example');





-- 6. patient_insurance
INSERT INTO patient_insurance (policy_id, patient_id, provider_id, policy_number, coverage_percent,
max_coverage_amount, valid_from, valid_to) VALUES
(1, 1, 1, 'SL-2026-0001', 80.00, 200000.00, '2026-01-01', '2026-12-31'),
(2, 3, 2, 'MA-2026-0107', 70.00, 150000.00, '2026-02-01', '2027-01-31'),
(3, 6, 1, 'SL-2026-0145', 90.00, 300000.00, '2026-03-01', '2027-02-28'),
(4, 7, 3, 'NC-2025-0333', 60.00, 100000.00, '2025-04-01', '2026-03-31'),
(5, 9, 2, 'MA-2026-0210', 75.00, 250000.00, '2026-05-01', '2027-04-30'),
(6, 11, 3, 'NC-2026-0451', 50.00, 120000.00, '2026-01-15', '2027-01-14');




--7. doctor_availability
INSERT INTO doctor_availability (availability_id, doctor_id, day_of_week, start_time, end_time, slot_minutes) VALUES
(1, 1, 'Mon', '09:00:00', '13:00:00', 30),
(2, 1, 'Wed', '09:00:00', '13:00:00', 30),
(3, 2, 'Tue', '10:00:00', '14:00:00', 30),
(4, 2, 'Thu', '10:00:00', '14:00:00', 30),
(5, 3, 'Mon', '14:00:00', '18:00:00', 30),
(6, 3, 'Fri', '14:00:00', '18:00:00', 30),
(7, 4, 'Tue', '09:00:00', '13:00:00', 30),
(8, 4, 'Sat', '09:00:00', '12:00:00', 30),
(9, 5, 'Mon', '10:00:00', '16:00:00', 30),
(10, 5, 'Wed', '10:00:00', '16:00:00', 30),
(11, 6, 'Wed', '11:00:00', '15:00:00', 30),
(12, 6, 'Fri', '11:00:00', '15:00:00', 30),
(13, 7, 'Thu', '09:00:00', '13:00:00', 30),
(14, 8, 'Tue', '14:00:00', '18:00:00', 30);





-- 8. appointment
INSERT INTO appointment (appointment_id, patient_id, doctor_id, appointment_date, appointment_time,
reason, status) VALUES
(1, 1, 1, '2026-10-05', '09:00:00', 'Chest pain on exertion','Completed'),
(2, 3, 1, '2026-10-05', '09:30:00', 'Hypertension follow-up','Completed'),
(3, 2, 2, '2026-10-06', '10:00:00', 'Palpitations','Completed'),
(4, 5, 4, '2026-10-06', '09:00:00', 'Fever and cough','Completed'),
(5, 8, 4, '2026-10-06', '09:30:00', 'Routine check-up','Completed'),
(6, 4, 6, '2026-10-07', '11:00:00', 'Recurring headaches','Completed'),
(7, 7, 5, '2026-10-07', '10:00:00', 'Diabetes review','Completed'),
(8, 9, 3, '2026-10-09', '14:00:00', 'Knee pain','Completed'),
(9, 10, 5, '2026-10-12', '10:00:00', 'Fever with chills','Completed'),
(10, 6, 6, '2026-10-14', '11:30:00', 'Severe migraine','Completed'),
(11, 11, 1, '2026-10-14', '09:00:00', 'Shortness of breath','Completed'),
(12, 12, 5, '2026-10-14', '10:30:00', 'Abdominal pain','Cancelled'),
(13, 2, 2, '2026-10-22', '10:30:00', 'Follow-up ECG','Scheduled'),
(14, 1, 1, '2026-10-21', '09:00:00', 'Review after tests','Scheduled'),
(15, 9, 3, '2026-10-23', '14:30:00', 'Knee X-ray review','Scheduled'),
(16, 7, 5, '2026-10-12', '11:00:00', 'Missed diabetes review','No-Show');






-- 9. medical_record
INSERT INTO medical_record (record_id, patient_id, doctor_id, appointment_id, visit_date, symptoms,
diagnosis, treatment_notes) VALUES
(1, 1, 1, 1, '2026-10-05', 'Chest pain on exertion', 'Stable angina', 'ECG and lipid profile advised; lifestyle changes'),
(2, 3, 1, 2, '2026-10-05', 'Headache, raised BP', 'Essential hypertension', 'Continue antihypertensive; reduce salt intake'),
(3, 2, 2, 3, '2026-10-06', 'Palpitations, dizziness', 'Sinus tachycardia', 'ECG done; review if symptoms persist'),
(4, 5, 4, 4, '2026-10-06', 'Fever and cough for 3 days', 'Acute bronchitis', 'Rest, fluids and a short antibiotic course'),
(5, 8, 4, 5, '2026-10-06', 'Routine check-up', 'No abnormality detected', 'Vitamin supplementation advised'),
(6, 4, 6, 6, '2026-10-07', 'Recurring headache, nausea', 'Migraine', 'MRI if no response to therapy'),
(7, 7, 5, 7, '2026-10-07', 'Thirst, fatigue', 'Type 2 diabetes mellitus', 'HbA1c and fasting glucose ordered'),
(8, 9, 3, 8, '2026-10-09', 'Right knee pain', 'Osteoarthritis of knee', 'X-ray ordered; physiotherapy advised'),
(9, 10, 5, 9, '2026-10-12', 'Fever with chills', 'Viral fever', 'CBC ordered; symptomatic treatment'),
(10, 6, 6, 10, '2026-10-14', 'Severe one-sided headache', 'Migraine with aura', 'Preventive therapy started'),
(11, 11, 1, 11, '2026-10-14', 'Shortness of breath', 'Congestive heart failure', 'Admission advised for monitoring'),
(12, 3, 5, NULL, '2026-09-28', 'Severe headache, BP 190/110','Hypertensive urgency', 'Emergency admission for IV therapy');


-- 10. medicine
INSERT INTO medicine (medicine_id, medicine_name, manufacturer, unit_price, stock_quantity) VALUES
(1, 'Paracetamol 500mg', 'Zenith Pharma',2.00, 500),
(2, 'Amoxicillin 500mg', 'Apex Labs',8.50, 300),
(3, 'Atorvastatin 10mg', 'Medico Remedies', 6.00, 400),
(4, 'Amlodipine 5mg','Zenith Pharma',4.50, 350),
(5, 'Metformin 500mg','Apex Labs',3.00, 600),
(6, 'Aspirin 75mg','Medico Remedies', 1.50, 800),
(7, 'Sumatriptan 50mg','Zenith Pharma',25.00, 120),
(8, 'Ibuprofen 400mg','Apex Labs',3.50, 450),
(9, 'Furosemide 40mg','Medico Remedies', 5.00, 200),
(10, 'Cetirizine 10mg','Zenith Pharma',2.50, 15);





-- 11. prescription
INSERT INTO prescription (prescription_id, record_id, prescribed_date, notes) VALUES
(1, 1, '2026-10-05', 'Review after lipid profile'),
(2, 2, '2026-10-05', 'Monitor blood pressure weekly'),
(3, 4, '2026-10-06', 'Complete the full course'),
(4, 6, '2026-10-07', NULL),
(5, 7, '2026-10-07', 'Take after meals'),
(6, 8, '2026-10-09', 'Avoid on an empty stomach'),
(7, 9, '2026-10-12', NULL),
(8, 11, '2026-10-14', 'Daily weight monitoring');





-- 12. prescription_item
INSERT INTO prescription_item (item_id, prescription_id, medicine_id, dosage, frequency,
duration_days, quantity) VALUES
(1, 1, 3, '10 mg', 'Once daily at night',30, 30),
(2, 1, 6, '75 mg', 'Once daily after breakfast', 30, 30),
(3, 2, 4, '5 mg','Once daily',30, 30),
(4, 3, 2, '500 mg', 'Three times daily',5, 15),
(5, 3, 1, '500 mg', 'Three times daily if fever', 3, 6),
(6, 4, 7, '50 mg', 'At onset of attack',10, 6),
(7, 5, 5, '500 mg', 'Twice daily after meals',30, 60),
(8, 6, 8, '400 mg', 'Twice daily after meals',7, 14),
(9, 7, 1, '500 mg', 'Three times daily',5, 15),
(10, 7, 10, '10 mg', 'Once daily at night',5, 5),
(11, 8, 9, '40 mg', 'Once daily in the morning', 14, 14),
(12, 8, 6, '75 mg', 'Once daily',14, 14);





-- 13. lab_test
INSERT INTO lab_test (test_id, test_name, normal_range, cost) VALUES
(1, 'Complete Blood Count (CBC)', 'WBC 4.5-11.0 x10^3/uL',350.00),
(2, 'Lipid Profile','LDL below 100 mg/dL',600.00),
(3, 'HbA1c','Below 5.7 %',500.00),
(4, 'ECG','Normal sinus rhythm',400.00),
(5, 'Fasting Blood Glucose','70-100 mg/dL',150.00),
(6, 'Knee X-ray','No structural abnormality', 700.00);





-- 14. lab_order
INSERT INTO lab_order (lab_order_id, record_id, test_id, ordered_date, result_value, result_date, status) VALUES
(1, 1, 4, '2026-10-05', 'ST depression noted', '2026-10-05', 'Completed'),
(2, 1, 2, '2026-10-05', 'LDL 148 mg/dL', '2026-10-06', 'Completed'),
(3, 3, 4, '2026-10-06', 'Sinus tachycardia', '2026-10-06', 'Completed'),
(4, 7, 3, '2026-10-07', '8.4 %', '2026-10-08', 'Completed'),
(5, 7, 5, '2026-10-07', '172 mg/dL', '2026-10-07', 'Completed'),
(6, 8, 6, '2026-10-09', 'Joint space narrowing', '2026-10-09', 'Completed'),
(7, 9, 1, '2026-10-12', 'WBC 5.2 x10^3/uL', '2026-10-12', 'Completed'),
(8, 11, 4, '2026-10-14', NULL, NULL, 'Ordered'),
(9, 11, 1, '2026-10-14', NULL, NULL, 'Ordered');





-- 15. ward
INSERT INTO ward (ward_id, ward_name, ward_type, floor_no, daily_charge) VALUES
(1, 'General Ward A', 'General',1, 1500.00),
(2, 'Private Wing B', 'Private',2, 4000.00),
(3, 'ICU','ICU',3, 9000.00),
(4, 'Maternity Ward', 'Maternity', 2, 3500.00);



-- 16. bed
INSERT INTO bed (bed_id, ward_id, bed_number, status) VALUES
(1, 1, 'G-101', 'Available'),
(2, 1, 'G-102', 'Available'),
(3, 1, 'G-103', 'Available'),
(4, 1, 'G-104', 'Available'),
(5, 2, 'P-201', 'Available'),
(6, 2, 'P-202', 'Occupied'),
(7, 2, 'P-203', 'Available'),
(8, 3, 'ICU-01', 'Occupied'),
(9, 3, 'ICU-02', 'Available'),
(10, 3, 'ICU-03', 'Available'),
(11, 4, 'M-401', 'Available'),
(12, 4, 'M-402', 'Available');





-- 17. admission
INSERT INTO admission (admission_id, patient_id, doctor_id, bed_id, admit_datetime, discharge_datetime,
reason, status, discharge_summary) VALUES
(1, 11, 1, 1, '2026-10-14 12:00:00', '2026-10-18 11:00:00',
'Congestive heart failure - monitoring and diuretic therapy', 'Discharged',
'Stabilised on oral diuretics; review in two weeks'),
(2, 3, 5, 5, '2026-09-28 10:00:00', '2026-10-02 12:00:00',
'Hypertensive urgency - IV therapy', 'Discharged',
'Blood pressure controlled; continue oral medication'),
(3, 7, 5, 8, '2026-10-15 20:30:00', NULL,
'Diabetic ketoacidosis', 'Admitted', NULL),
(4, 9, 3, 2, '2026-10-16 09:00:00', '2026-10-17 15:00:00',
'Right knee arthroscopy', 'Discharged',
'Uneventful recovery; physiotherapy advised'),
(5, 6, 6, 6, '2026-10-19 08:00:00', NULL,
'Observation for migraine with neurological symptoms', 'Admitted', NULL);











-- 18. bill
INSERT INTO bill (bill_id, patient_id, appointment_id, admission_id, bill_date, total_amount,
insurance_covered, payment_status) VALUES
(1, 1, 1,NULL, '2026-10-05', 2025.00, 1620.00, 'Paid'),
(2, 3, 2,NULL, '2026-10-05',935.00,654.50, 'Paid'),
(3, 2, 3,NULL, '2026-10-06', 1300.00,0.00, 'Paid'),
(4, 5, 4,NULL, '2026-10-06',639.50,0.00, 'Partial'),
(5, 7, 7,NULL, '2026-10-07', 1230.00,0.00, 'Paid'),
(6, 9, 8,NULL, '2026-10-09', 1449.00, 1086.75, 'Pending'),
(7, 10, 9,NULL, '2026-10-12',792.50,0.00, 'Paid'),
(8, 3, NULL, 2,'2026-10-02', 19400.00, 13580.00, 'Paid'),
(9, 11, NULL, 1,'2026-10-18', 9791.00, 4895.50, 'Paid'),
(10, 9, NULL, 4,'2026-10-17', 47200.00, 35400.00, 'Partial');







-- 19. bill_item
INSERT INTO bill_item (bill_item_id, bill_id, item_type, description, quantity, unit_price) VALUES
(1, 1, 'Consultation', 'Cardiology consultation - Dr. Anil Sharma',1,800.00),
(2, 1, 'Lab Test','ECG',1,400.00),
(3, 1, 'Lab Test','Lipid Profile',1,600.00),
(4, 1, 'Medicine','Atorvastatin 10mg',30,6.00),
(5, 1, 'Medicine','Aspirin 75mg',30,1.50),
(6, 2, 'Consultation', 'Cardiology consultation - Dr. Anil Sharma',1,800.00),
(7, 2, 'Medicine','Amlodipine 5mg',30,4.50),
(8, 3, 'Consultation', 'Cardiology consultation - Dr. Meera Kapoor',1,900.00),
(9, 3, 'Lab Test','ECG',1,400.00),
(10, 4, 'Consultation', 'Pediatric consultation - Dr. Sunita Rao',1,500.00),
(11, 4, 'Medicine','Amoxicillin 500mg',15,8.50),
(12, 4, 'Medicine','Paracetamol 500mg',6,2.00),
(13, 5, 'Consultation', 'General consultation - Dr. Vikram Singh',1,400.00),
(14, 5, 'Lab Test','HbA1c',1,500.00),
(15, 5, 'Lab Test','Fasting Blood Glucose',1,150.00),
(16, 5, 'Medicine','Metformin 500mg',60,3.00),
(17, 6, 'Consultation', 'Orthopedic consultation - Dr. Rajesh Verma',1,700.00),
(18, 6, 'Lab Test','Knee X-ray',1,700.00),
(19, 6, 'Medicine','Ibuprofen 400mg',14,3.50),
(20, 7, 'Consultation', 'General consultation - Dr. Vikram Singh',1,400.00),
(21, 7, 'Lab Test','Complete Blood Count (CBC)',1,350.00),
(22, 7, 'Medicine','Paracetamol 500mg',15,2.00),
(23, 7, 'Medicine','Cetirizine 10mg',5,2.50),
(24, 8, 'Room Charge', 'Private Wing B - 4 day(s)',4, 4000.00),
(25, 8, 'Consultation', 'General consultation - Dr. Vikram Singh',1,400.00),
(26, 8, 'Procedure','IV therapy and monitoring',1, 3000.00),
(27, 9, 'Room Charge', 'General Ward A - 4 day(s)',4, 1500.00),
(28, 9, 'Consultation', 'Cardiology consultation - Dr. Anil Sharma',1,800.00),
(29, 9, 'Lab Test','ECG',1,400.00),
(30, 9, 'Medicine','Furosemide 40mg',14,5.00),
(31, 9, 'Medicine','Aspirin 75mg',14,1.50),
(32, 9, 'Procedure','Cardiac monitoring',1, 2500.00),
(33, 10, 'Room Charge', 'General Ward A - 1 day(s)',1, 1500.00),
(34, 10, 'Procedure','Knee arthroscopy',1, 45000.00),
(35, 10, 'Consultation', 'Orthopedic consultation - Dr. Rajesh Verma',1,700.00);








-- 20. payment
INSERT INTO payment (payment_id, bill_id, payment_date, amount, payment_mode, reference_no) VALUES
(1, 1, '2026-10-05 11:30:00', 405.00, 'UPI','UPI-1001'),
(2, 2, '2026-10-05 12:10:00', 280.50, 'Cash',NULL),
(3, 3, '2026-10-06 11:45:00', 1300.00, 'UPI','UPI-1002'),
(4, 4, '2026-10-06 10:15:00', 400.00, 'Cash', NULL),
(5, 5, '2026-10-07 12:20:00', 1230.00, 'Card','CARD-5521'),
(6, 7, '2026-10-12 12:00:00', 792.50, 'Cash',NULL),
(7, 8, '2026-10-02 12:30:00', 5820.00, 'Net Banking', 'NB-7781'),
(8, 9, '2026-10-18 11:30:00', 3000.00, 'Card','CARD-5530'),
(9, 9, '2026-10-19 10:00:00', 1895.50, 'UPI','UPI-1010'),
(10, 10, '2026-10-17 16:00:00', 5000.00, 'Card','CARD-5529');








-- 21. Post-load UPDATE statements
-- UPDATE demonstration: fill the columns that were added with ALTER TABLE
UPDATE doctor
SET qualification = CASE doctor_id
WHEN 1 THEN 'MD, DM (Cardiology)'
WHEN 2 THEN 'MD, DM (Cardiology)'
WHEN 3 THEN 'MS (Orthopedics)'
Hospital Management & Patient Care System | [Roll Number] | Building Database Applications
Page 30WHEN 4 THEN 'MD (Pediatrics)'
WHEN 5 THEN 'MBBS, MD (Medicine)'
WHEN 6 THEN 'MD, DM (Neurology)'
WHEN 7 THEN 'MBBS'
WHEN 8 THEN 'MD (Pediatrics)'
END;
UPDATE medicine SET reorder_level = 250 WHERE medicine_id = 9;
-- Furosemide: higher demand
-- Take one maternity-ward bed out of service for maintenance
UPDATE bed SET status = 'Maintenance' WHERE bed_id = 12;