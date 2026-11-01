-- 1. sp_apply_insurance
-- Purpose: Applies the patient's valid insurance policy to a bill: covered = MIN(total x coverage %, policy maximum).
CREATE PROCEDURE sp_apply_insurance(IN p_bill_id INT)
BEGIN
DECLARE v_patient INT;
DECLARE v_total DECIMAL(12,2);
DECLARE v_date DATE;
DECLARE v_percent DECIMAL(5,2) DEFAULT 0;
DECLARE v_max DECIMAL(12,2) DEFAULT 0;
DECLARE v_cover DECIMAL(12,2) DEFAULT 0;
DECLARE v_balance DECIMAL(12,2);
DECLARE v_status VARCHAR(10);

SELECT patient_id, total_amount, bill_date INTO v_patient, v_total, v_date
FROM bill WHERE bill_id = p_bill_id;

IF v_patient IS NULL THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Bill not found';
END IF;

-- best policy that is valid on the bill date (stays 0 / 0 when none exists)
SELECT coverage_percent, max_coverage_amount INTO v_percent, v_max
FROM patient_insurance
WHERE patient_id = v_patient AND v_date BETWEEN valid_from AND valid_to
ORDER BY coverage_percent DESC
LIMIT 1;

SET v_cover = LEAST(ROUND(v_total * v_percent / 100, 2), v_max);

UPDATE bill SET insurance_covered = v_cover WHERE bill_id = p_bill_id;

SET v_balance = fn_bill_balance(p_bill_id);
SET v_status = CASE
    WHEN v_total > 0 AND v_balance <= 0
    THEN 'Paid'
    WHEN EXISTS (SELECT 1 FROM payment WHERE bill_id = p_bill_id) THEN 'Partial'
    ELSE 'Pending' END;
UPDATE bill SET payment_status = v_status WHERE bill_id = p_bill_id;
END$$





-- 2. sp_book_appointment
-- Purpose: Books an appointment; slot validity is enforced by trg_appointment_bi.
CREATE PROCEDURE sp_book_appointment(
    IN p_patient_id INT,
    IN p_doctor_id INT,
    IN p_date DATE,
    IN p_time TIME,
    IN p_reason VARCHAR(200),
    OUT p_appointment_id INT)

BEGIN

    IF p_date < CURDATE() THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Cannot book an appointment in the past';
    END IF;

    INSERT INTO appointment (patient_id, doctor_id, appointment_date, appointment_time, 
                            reason, status)
    VALUES (p_patient_id, p_doctor_id, p_date, p_time, p_reason, 'Scheduled');

    SET p_appointment_id = LAST_INSERT_ID();

    END$$





-- 3. sp_admit_patient
--Purpose: Admits a patient to a bed inside a transaction; the bed row is locked to prevent double allotment.
CREATE PROCEDURE sp_admit_patient(
IN p_patient_id INT,
IN p_doctor_id INT,
IN p_bed_id INT,
IN p_reason VARCHAR(200),
OUT p_admission_id INT)

BEGIN
    DECLARE v_status VARCHAR(20);

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;
    
    START TRANSACTION;
    
    SELECT status INTO v_status FROM bed WHERE bed_id = p_bed_id FOR UPDATE;
    
    IF v_status IS NULL OR v_status <> 'Available' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Selected bed is not available';
    END IF;
    
    INSERT INTO admission (patient_id, doctor_id, bed_id, admit_datetime, reason, status)
    VALUES (p_patient_id, p_doctor_id, p_bed_id, NOW(), p_reason, 'Admitted');
    
    SET p_admission_id = LAST_INSERT_ID();
    COMMIT;
    END$$
-- trg_admission_ai marks the bed Occupied




-- 4. sp_discharge_patient
-- Purpose: Discharges a patient, frees the bed, generates the room-charge bill and applies insurance - atomically.
CREATE PROCEDURE sp_discharge_patient(
    IN p_admission_id INT,
    IN p_summary TEXT,
    IN p_discharge_time DATETIME,      -- NULL means NOW()
    OUT p_bill_id INT)
BEGIN
    DECLARE v_status VARCHAR(20);
    DECLARE v_patient INT;
    DECLARE v_bed INT;
    DECLARE v_days INT;
    DECLARE v_rate DECIMAL(8,2);
    DECLARE v_ward VARCHAR(50);
    DECLARE v_when DATETIME;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;
    
    SET v_when = COALESCE(p_discharge_time, NOW());

    START TRANSACTION;

    SELECT status, patient_id, bed_id INTO v_status, v_patient, v_bed
    FROM admission WHERE admission_id = p_admission_id FOR UPDATE;
    
    IF v_status IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Admission not found';
    ELSEIF v_status = 'Discharged' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Patient is already discharged';
    END IF;
    
    UPDATE admission
    SET discharge_datetime = v_when, status = 'Discharged', discharge_summary = p_summary
    WHERE admission_id = p_admission_id;
    
    UPDATE bed SET status = 'Available' WHERE bed_id = v_bed;
    SET v_days = fn_length_of_stay(p_admission_id);
    
    SELECT w.daily_charge, w.ward_name INTO v_rate, v_ward
    FROM bed b JOIN ward w ON w.ward_id = b.ward_id
    WHERE b.bed_id = v_bed;
    
    INSERT INTO bill (patient_id, admission_id, bill_date)
    VALUES (v_patient, p_admission_id, DATE(v_when));
    SET p_bill_id = LAST_INSERT_ID();
    
    INSERT INTO bill_item (bill_id, item_type, description, quantity, unit_price)
    VALUES (p_bill_id, 'Room Charge', CONCAT(v_ward, ' - ', v_days, ' day(s)'), v_days, v_rate);
    
    CALL sp_apply_insurance(p_bill_id);
    COMMIT;
END$$
-- bill total was updated by trg_bill_item_ai



-- 5. sp_dispense_prescription
-- Purpose: Dispenses all items of a prescription and deducts pharmacy stock; rolls back if any item is short.
CREATE PROCEDURE sp_dispense_prescription(IN p_prescription_id INT)
BEGIN
    DECLARE v_done INT DEFAULT 0;
    DECLARE v_med INT;
    DECLARE v_qty INT;
    DECLARE v_stock INT;
    
    DECLARE cur CURSOR FOR
        SELECT medicine_id, quantity
        FROM prescription_item
        WHERE prescription_id = p_prescription_id
        ORDER BY item_id;
        
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_done = 1;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION

    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    START TRANSACTION;
    OPEN cur;

    item_loop: LOOP
        FETCH cur INTO v_med, v_qty;

        IF v_done = 1 THEN
        LEAVE item_loop;
        END IF;

        SELECT stock_quantity INTO v_stock
        FROM medicine WHERE medicine_id = v_med FOR UPDATE;

        IF v_stock < v_qty THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Insufficient stock to dispense the prescription';
        END IF;

        UPDATE medicine SET stock_quantity = stock_quantity - v_qty
        WHERE medicine_id = v_med;
        
    END LOOP;

    CLOSE cur;
    COMMIT;
END$$