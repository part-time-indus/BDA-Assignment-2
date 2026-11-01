-- 1. fn_patient_age
-- Purpose: Returns the current age (in years) of a patient. Age is derived, never stored.
CREATE FUNCTION fn_patient_age(p_patient_id INT)
RETURNS INT
READS SQL DATA

BEGIN
    DECLARE v_age INT;
    
    SELECT TIMESTAMPDIFF(YEAR, date_of_birth, CURDATE()) INTO v_age
    FROM patient
    WHERE patient_id = p_patient_id;
    
    RETURN v_age;
END$$




-- 2. fn_bill_balance
-- Purpose: Returns the amount a patient still owes on a bill (total - insurance - payments).
CREATE FUNCTION fn_bill_balance(p_bill_id INT)
RETURNS DECIMAL(12,2)
READS SQL DATA

BEGIN
    DECLARE v_payable DECIMAL(12,2);
    DECLARE v_paid
    DECIMAL(12,2);

    SELECT total_amount - insurance_covered INTO v_payable
    FROM bill WHERE bill_id = p_bill_id;

    SELECT COALESCE(SUM(amount), 0) INTO v_paid
    FROM payment WHERE bill_id = p_bill_id;
    
    RETURN v_payable - v_paid;
END$$
-- NULL when the bill does not exist






-- 3. fn_length_of_stay
-- Purpose: Number of billable days of an admission (minimum 1; open admissions run to today).
CREATE FUNCTION fn_length_of_stay(p_admission_id INT)
RETURNS INT
READS SQL DATA

BEGIN
    DECLARE v_days INT;
    
    SELECT GREATEST(1, DATEDIFF(COALESCE(DATE(discharge_datetime), CURDATE()),
    DATE(admit_datetime))) INTO v_days
    FROM admission
    WHERE admission_id = p_admission_id;
    
    RETURN v_days;
END$$







-- 4. fn_is_slot_available
-- Purpose: Checks that a doctor is active, works at the requested day/time and the slot is free.
CREATE FUNCTION fn_is_slot_available(p_doctor_id INT, p_date DATE, p_time TIME)
RETURNS TINYINT(1)
READS SQL DATA

BEGIN
    DECLARE v_active INT DEFAULT 0;
    DECLARE v_in_schedule INT DEFAULT 0;
    DECLARE v_booked INT DEFAULT 0;

    SELECT COUNT(*) INTO v_active
    FROM doctor WHERE doctor_id = p_doctor_id AND status = 'Active';
    
    SELECT COUNT(*) INTO v_in_schedule
    FROM doctor_availability
    WHERE doctor_id = p_doctor_id
    AND day_of_week = DATE_FORMAT(p_date, '%a')
    AND p_time >= start_time AND p_time < end_time;
    
    SELECT COUNT(*) INTO v_booked
    FROM appointment
    WHERE doctor_id = p_doctor_id AND appointment_date = p_date
    AND appointment_time = p_time AND status IN ('Scheduled', 'Completed');

    RETURN (v_active = 1 AND v_in_schedule > 0 AND v_booked = 0);
END$$


DELIMITER ;

-- SELECT fn_patient_age(7)
-- AS age_of_patient_7,    -- 68
-- fn_bill_balance(4)
-- AS balance_of_bill_4,   -- 239.50
-- fn_length_of_stay(1) AS days_of_admission_1,  -- 4
-- fn_is_slot_available(1, '2026-10-21', '09:30:00') AS slot_free;  -- 1