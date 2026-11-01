CREATE INDEX idx_patient_name ON patient (last_name, first_name);

CREATE INDEX idx_appt_patient_date ON appointment (patient_id, appointment_date);

CREATE INDEX idx_appt_status_date ON appointment (status, appointment_date);

CREATE INDEX idx_record_visit_date ON medical_record (visit_date);

CREATE INDEX idx_bill_status_date ON bill (payment_status, bill_date);

CREATE INDEX idx_admission_status ON admission (status);

CREATE INDEX idx_medicine_stock ON medicine (stock_quantity);

EXPLAIN SELECT appointment_id, patient_id, appointment_date
FROM appointment
WHERE status = 'Scheduled' AND appointment_date BETWEEN '2026-10-20' AND '2026-10-31';

SHOW INDEX FROM appointment;