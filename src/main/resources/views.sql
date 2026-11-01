1-- 1. vw_patient_summary
-- Purpose: One row per patient with derived age and activity counts (front-desk lookup).
CREATE OR REPLACE VIEW vw_patient_summary AS
SELECT  p.patient_id,
        CONCAT(p.first_name, ' ', p.last_name) AS patient_name,

        p.gender,

        fn_patient_age(p.patient_id) AS age,
        p.blood_group,
        p.city,
        p.phone,

        COUNT(DISTINCT a.appointment_id) AS total_appointments,
        COUNT(DISTINCT ad.admission_id) AS total_admissions

FROM patient p

LEFT JOIN appointment a ON a.patient_id = p.patient_id

LEFT JOIN admission ad ON ad.patient_id = p.patient_id

GROUP BY p.patient_id;





-- 2. vw_appointment_details
-- Purpose: Appointments joined with patient, doctor and department names (reception desk and reports).
CREATE OR REPLACE VIEW vw_appointment_details AS
SELECT  a.appointment_id,
        a.appointment_date,
        a.appointment_time,
        a.status,
        
        CONCAT(p.first_name, ' ', p.last_name) AS patient_name,
        
        CONCAT('Dr. ', d.first_name, ' ', d.last_name) AS doctor_name,
        
        d.specialization,
        dp.dept_name,
        a.reason
FROM appointment a

JOIN patient p ON p.patient_id = a.patient_id

JOIN docto rd ON d.doctor_id = a.doctor_id

JOIN department dp ON dp.dept_id= d.dept_id;






-- 3. vw_current_admissions
-- Purpose: In-patients currently admitted, with ward, bed and days stayed (ward round / nursing).
CREATE OR REPLACE VIEW vw_current_admissions AS
SELECT ad.admission_id,
        
        CONCAT(p.first_name, ' ', p.last_name) AS patient_name,
        
        CONCAT('Dr. ', d.first_name, ' ', d.last_name) AS doctor_name,
        
        w.ward_name,
        b.bed_number,
        ad.admit_datetime,

        fn_length_of_stay(ad.admission_id) AS days_admitted,
        
        ad.reason
FROM admission ad

JOIN patient p ON p.patient_id = ad.patient_id

JOIN doctor d ON d.doctor_id = ad.doctor_id

JOIN bed b ON b.bed_id = ad.bed_id

JOIN ward w ON w.ward_id = b.ward_id

WHERE ad.status = 'Admitted';






-- 4. vw_bill_summary
-- Purpose: Every bill with insurance share, amount paid and balance due (billing counter).
CREATE OR REPLACE VIEW vw_bill_summary AS
SELECT  b.bill_id,
        CONCAT(p.first_name, ' ', p.last_name) AS patient_name,

        b.bill_date,
        b.total_amount,
        b.insurance_covered,
        
        (b.total_amount - b.insurance_covered) AS patient_payable,
        
        COALESCE(pay.paid, 0) AS amount_paid,
        
        (b.total_amount - b.insurance_covered - COALESCE(pay.paid, 0)) AS balance_due,b.payment_status

FROM bill b

JOIN patient p ON p.patient_id = b.patient_id

LEFT JOIN (SELECT bill_id, SUM(amount) AS paid FROM payment

GROUP BY bill_id) pay ON pay.bill_id = b.bill_id;






-- 5. vw_bill_doctor
-- Purpose: Helper view: maps every bill to the doctor responsible (via appointment or admission).
CREATE OR REPLACE VIEW vw_bill_doctor AS
SELECT  b.bill_id,
        b.patient_id,
        b.bill_date,
        b.total_amount,
        b.insurance_covered,
        
        COALESCE(a.doctor_id, ad.doctor_id) AS doctor_id
        
FROM bill b
LEFT JOIN appointment a ON a.appointment_id = b.appointment_id

LEFT JOIN admission ad ON ad.admission_id = b.admission_id;





-- 6. vw_department_revenue
-- Purpose: Revenue, insurance share and patient share per department (management).
CREATE OR REPLACE VIEW vw_department_revenue AS
SELECT  dp.dept_id,
        dp.dept_name,

        COUNT(bd.bill_id) AS bill_count,
        
        COALESCE(SUM(bd.total_amount), 0) AS gross_revenue,
        
        COALESCE(SUM(bd.insurance_covered), 0) AS insurance_share,
        
        COALESCE(SUM(bd.total_amount - bd.insurance_covered), 0) AS patient_share

FROM department dp

LEFT JOIN doctor d ON d.dept_id = dp.dept_id

LEFT JOIN vw_bill_doctor bd ON bd.doctor_id = d.doctor_id

GROUP BY dp.dept_id, dp.dept_name;






-- 7. vw_medicine_stock_status
-- Purpose: Stock value and re-order status of every medicine (pharmacy).
CREATE OR REPLACE VIEW vw_medicine_stock_status AS
SELECT  medicine_id,
        medicine_name,
        manufacturer,
        stock_quantity,
        reorder_level,
        unit_price,

        (stock_quantity * unit_price) AS stock_value,
        
        CASE WHEN stock_quantity = 0 THEN 'OUT OF STOCK'
             WHEN stock_quantity <= reorder_level THEN 'REORDER'
             ELSE 'OK' END AS stock_status

FROM medicine;








-- 8. vw_ward_occupancy
-- Purpose: Bed counts and occupancy percentage per ward (nursing administration).
CREATE OR REPLACE VIEW vw_ward_occupancy AS
SELECT  w.ward_id,
        w.ward_name,
        w.ward_type,

        COUNT(b.bed_id) AS total_beds,
       
        COALESCE(SUM(b.status = 'Occupied'), 0) AS occupied_beds,
        
        COALESCE(SUM(b.status = 'Available'), 0) AS available_beds,
        
        COALESCE(SUM(b.status = 'Maintenance'), 0) AS maintenance_beds,
        
        ROUND(100 * COALESCE(SUM(b.status = 'Occupied'), 0)
        / NULLIF(COUNT(b.bed_id), 0), 1) AS occupancy_percent

FROM ward w

LEFT JOIN bed b ON b.ward_id = w.ward_id

GROUP BY w.ward_id, w.ward_name, w.ward_type;


SELECT * FROM vw_patient_summary;

SELECT * FROM vw_current_admissions;

SELECT * FROM vw_bill_summary WHERE payment_status <> 'Paid';
