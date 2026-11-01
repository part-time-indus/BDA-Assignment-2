-- 1a. Successful admission (patient 12 -> bed 3, ward General Ward A)
START TRANSACTION;

SELECT bed_id, bed_number, status FROM bed WHERE bed_id = 3 FOR UPDATE; -- row lock

INSERT INTO admission (patient_id, doctor_id, bed_id, admit_datetime, reason, status)
VALUES (12, 5, 3, NOW(), 'Acute abdominal pain - observation', 'Admitted');

SELECT bed_id, status FROM bed WHERE bed_id = 3;

COMMIT;-- now 'Occupied' (set by trigger)


-- 1b. Failed admission (bed 8 / ICU-01 is already occupied)
START TRANSACTION;

INSERT INTO admission (patient_id, doctor_id, bed_id, admit_datetime, reason, status)
VALUES (1, 1, 8, NOW(), 'Chest pain', 'Admitted'); -- ERROR 1644 (45000): Selected bed is not available

ROLLBACK;

SELECT COUNT(*) AS admissions_for_patient_1 FROM admission WHERE patient_id = 1;-- 0





-- 2.Discharge and final billing
-- sp_discharge_patient performs five dependent steps in ONE transaction: close the admission, free the bed, create the
-- bill, add the room-charge item (the trigger updates the total) and apply insurance. If any step fails, the EXIT HANDLER
-- issues ROLLBACK and nothing is saved.
CALL sp_discharge_patient(5, 'Migraine controlled; discharged on preventive therapy',
'2026-10-21 10:00:00', @bill_id);

SELECT @bill_id AS new_bill;

SELECT bill_id, total_amount, insurance_covered, payment_status
FROM bill WHERE bill_id = @bill_id; -- 8000.00 / 7200.00 / Pending

SELECT description, quantity, unit_price, amount
FROM bill_item WHERE bill_id = @bill_id; -- Private Wing B - 2 day(s)

SELECT bed_id, status FROM bed WHERE bed_id = 6; -- Available again




-- 3. Transaction 3 - Dispense a prescription (stock update) with rollback
-- All items of a prescription are issued and stock is reduced atomically. In the failure case Cetirizine stock is deliberately
-- set too low: the procedure raises an error after Paracetamol was already deducted, and ROLLBACK restores it -
-- atomicity in action.
-- 3a. Success: prescription 8 (Furosemide x14, Aspirin x14)
SELECT medicine_id, medicine_name, stock_quantity FROM medicine WHERE medicine_id IN (6, 9);
CALL sp_dispense_prescription(8);

SELECT medicine_id, medicine_name, stock_quantity FROM medicine WHERE medicine_id IN (6, 9); -- Furosemide 200 -> 186, Aspirin 800 -> 786



-- 3b. Failure: prescription 7 needs Paracetamol x15 and Cetirizine x5
UPDATE medicine SET stock_quantity = 2 WHERE medicine_id = 10;
CALL sp_dispense_prescription(7); -- ERROR 1644 (45000): Insufficient stock to dispense the prescription

SELECT medicine_id, stock_quantity FROM medicine WHERE medicine_id IN (1, 10); -- Paracetamol still 500 (rolled back), Cetirizine 2

UPDATE medicine SET stock_quantity = 15 WHERE medicine_id = 10; -- restore test data









-- 4. Part payment with SAVEPOINT
-- Two payments are recorded against bill 11 (balance 800.00). The second 500.00 payment would overpay the bill, so
-- trg_payment_bi rejects it; ROLLBACK TO SAVEPOINT undoes only that step, a corrected payment is posted and the
-- whole unit is committed.
START TRANSACTION;
INSERT INTO payment (bill_id, amount, payment_mode, reference_no)
VALUES (11, 500.00, 'UPI', 'UPI-2001'); -- balance now 300.00 -> status Partial SAVEPOINT after_first_payment;

INSERT INTO payment (bill_id, amount, payment_mode, reference_no)
VALUES (11, 500.00, 'Card', 'CARD-2002'); -- ERROR 1644 (45000): Payment exceeds the outstanding bill balance


ROLLBACK TO SAVEPOINT after_first_payment; -- keep the first payment only

INSERT INTO payment (bill_id, amount, payment_mode, reference_no)
VALUES (11, 300.00, 'Card', 'CARD-2002'); -- exact balance -> status Paid

COMMIT;

SELECT bill_id, payment_status, fn_bill_balance(bill_id) AS balance FROM bill WHERE bill_id = 11;