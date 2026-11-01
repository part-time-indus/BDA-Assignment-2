-- 1. trg_patient_bi
-- Purpose: BEFORE INSERT on patient - rejects a date of birth in the future.
CREATE TRIGGER trg_patient_bi
BEFORE INSERT ON patient
FOR EACH ROW
BEGIN
    IF NEW.date_of_birth > CURDATE() THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Date of birth cannot be in the future';
    END IF;
END$$




-- 2. trg_appointment_bi
-- Purpose: BEFORE INSERT on appointment - allows a new booking only in a free slot of an active doctor's
-- schedule.
CREATE TRIGGER trg_appointment_bi
BEFORE INSERT ON appointment
FOR EACH ROW
BEGIN
    IF NEW.status = 'Scheduled'
    AND fn_is_slot_available(NEW.doctor_id, NEW.appointment_date, NEW.appointment_time) = 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Doctor is not available at the requested date/time';
    END IF;
END$$




-- 3. trg_admission_bi
-- Purpose: BEFORE INSERT on admission - an admission is allowed only into an Available bed.
CREATE TRIGGER trg_admission_bi
BEFORE INSERT ON admission
FOR EACH ROW
BEGIN
    DECLARE v_status VARCHAR(20);
    
    IF NEW.status = 'Admitted' THEN
    
    SELECT status INTO v_status FROM bed WHERE bed_id = NEW.bed_id;
        
        IF v_status IS NULL OR v_status <> 'Available' THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Selected bed is not available';
        END IF;

    END IF;
END$$







-- 4. trg_admission_ai
-- Purpose: AFTER INSERT on admission - marks the allotted bed as Occupied.
CREATE TRIGGER trg_admission_ai
AFTER INSERT ON admission
FOR EACH ROW
BEGIN
    IF NEW.status = 'Admitted' THEN
        UPDATE bed SET status = 'Occupied' WHERE bed_id = NEW.bed_id;
    END IF;
END$$






 
-- 5. trg_bill_item_ai
-- Purpose: AFTER INSERT on bill_item - keeps bill.total_amount equal to the sum of its items.
CREATE TRIGGER trg_bill_item_ai
AFTER INSERT ON bill_item
FOR EACH ROW
BEGIN
    UPDATE bill
        SET total_amount = total_amount + (NEW.quantity * NEW.unit_price)
    WHERE bill_id = NEW.bill_id;
END$$








-- 6. trg_payment_bi
-- Purpose: BEFORE INSERT on payment - blocks a payment larger than the outstanding balance.
CREATE TRIGGER trg_payment_bi
BEFORE INSERT ON payment
FOR EACH ROW
BEGIN
    DECLARE v_balance DECIMAL(12,2);

    
    SET v_balance = fn_bill_balance(NEW.bill_id);
    
    IF v_balance IS NOT NULL AND NEW.amount > v_balance THEN
    
        SIGNAL SQLSTATE '45000'
    
        SET MESSAGE_TEXT = 'Payment exceeds the outstanding bill balance';
    
    END IF;
END$$







-- 7. trg_payment_ai
-- Purpose: AFTER INSERT on payment - sets the bill to Paid or Partial after each payment.
CREATE TRIGGER trg_payment_ai
AFTER INSERT ON payment
FOR EACH ROW
BEGIN
    DECLARE v_balance DECIMAL(12,2);

    SET v_balance = fn_bill_balance(NEW.bill_id);
    
    UPDATE bill
    SET payment_status = IF(v_balance <= 0, 'Paid', 'Partial')
    WHERE bill_id = NEW.bill_id;
END$$