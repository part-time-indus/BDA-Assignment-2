-- 1: Patient Treatment Summary
-- Audience / purpose: Visits, last visit, diagnoses and total billed for every patient.
SELECT p.patient_id,
    CONCAT(p.first_name, ' ', p.last_name) AS patient,
    
    fn_patient_age(p.patient_id) AS age,
    
    COUNT(DISTINCT mr.record_id) AS visits,
    MAX(mr.visit_date) AS last_visit,
    
    GROUP_CONCAT(DISTINCT mr.diagnosis ORDER BY mr.diagnosis SEPARATOR '; ') AS diagnoses,
    
    (SELECT COALESCE(SUM(b.total_amount), 0)
        FROM bill b WHERE b.patient_id = p.patient_id) AS total_billed

FROM patient p LEFT JOIN medical_record mr ON mr.patient_id = p.patient_id

GROUP BY p.patient_id, p.first_name, p.last_name

ORDER BY p.patient_id;





-- 2: Doctor Workload Report
-- Audience / purpose: Appointments handled by each doctor, split by status, with completion percentage.
SELECT d.doctor_id,
    CONCAT('Dr. ', d.first_name, ' ', d.last_name) AS doctor,
     
    dp.dept_name,
    
    COUNT(a.appointment_id) AS total,
    
    COALESCE(SUM(a.status = 'Completed'), 0) AS completed,
    
    COALESCE(SUM(a.status = 'Scheduled'), 0) AS scheduled,
   
    COALESCE(SUM(a.status = 'Cancelled'), 0) AS cancelled,
   
    COALESCE(SUM(a.status = 'No-Show'), 0) AS no_show,
   
    ROUND(100 * SUM(a.status = 'Completed') / NULLIF(COUNT(a.appointment_id), 0), 1) AS completion_pct
FROM doctor d

JOIN department dp ON dp.dept_id = d.dept_id

LEFT JOIN appointment a ON a.doctor_id = d.doctor_id

GROUP BY d.doctor_id, d.first_name, d.last_name, dp.dept_name

ORDER BY d.doctor_id;




-- 3: Department Revenue Report
-- Audience / purpose: Gross revenue, insurer share and patient share by department (view).
SELECT * FROM vw_department_revenue ORDER BY gross_revenue DESC;




-- Report 4: Ward Occupancy Report
-- Audience / purpose: Bed utilisation of every ward (view).
SELECT * FROM vw_ward_occupancy ORDER BY ward_id;




-- Report 5: Pharmacy Stock and Re-order Report
-- Audience / purpose: Stock value and re-order status of each medicine (view); alerts first.
SELECT medicine_name, stock_quantity, reorder_level, stock_value, stock_status
FROM vw_medicine_stock_status
ORDER BY stock_status = 'OK', medicine_name;





-- 6: Outstanding Payments Report
-- Audience / purpose: Bills with a balance still due, largest first (view).
SELECT bill_id, patient_name, bill_date, patient_payable, amount_paid, balance_due, payment_status
FROM vw_bill_summary
WHERE balance_due > 0
ORDER BY balance_due DESC;





-- 7: Monthly Billing Summary
-- Audience / purpose: Bills raised per month with insurer and patient shares.
SELECT DATE_FORMAT(b.bill_date, '%Y-%m')
    COUNT(*)
    SUM(b.total_amount)
    SUM(b.insurance_covered)
    SUM(b.total_amount - b.insurance_covered)
FROM bill b
GROUP BY DATE_FORMAT(b.bill_date, '%Y-%m')
ORDER BY bill_month;





-- 8: Insurance Claims Report
-- Audience / purpose: Policies, claimed bills and total amount claimed per insurance provider.
SELECT ip.provider_name,
    COUNT(DISTINCT pi.policy_id) AS policies,
    COUNT(b.bill_id) AS claimed_bills,
    COALESCE(SUM(b.insurance_covered), 0) AS total_claimed
FROM insurance_provider ip

LEFT JOIN patient_insurance pi ON pi.provider_id = ip.provider_id

LEFT JOIN bill b ON b.patient_id = pi.patient_id
AND b.bill_date BETWEEN pi.valid_from AND pi.valid_to

GROUP BY ip.provider_id, ip.pr