-- Test seed data for RBAC integration tests
-- Fixed UUIDs for deterministic test assertions

-- Profiles
INSERT INTO profiles (id, first_name, last_name, email, phone_number, date_of_birth, created_at, updated_at)
VALUES ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'Patient', 'Test', 'patient@test.com', '555-0001', '1990-01-01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO profiles (id, first_name, last_name, email, phone_number, date_of_birth, created_at, updated_at)
VALUES ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'Doctor', 'Test', 'doctor@test.com', '555-0002', '1985-06-15', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO profiles (id, first_name, last_name, email, phone_number, date_of_birth, created_at, updated_at)
VALUES ('cccccccc-cccc-cccc-cccc-cccccccccccc', 'Admin', 'Test', 'admin@test.com', '555-0003', '1980-03-20', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO profiles (id, first_name, last_name, email, phone_number, date_of_birth, created_at, updated_at)
VALUES ('22222222-2222-2222-2222-222222222222', 'Other', 'Patient', 'other@test.com', '555-0004', '1992-07-10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Patient entities
INSERT INTO patient_entity (id, profile_id, medical_record_number, notes, active, created_at, updated_at)
VALUES ('dddddddd-dddd-dddd-dddd-dddddddddddd', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'MRN-TEST-001', 'Test patient', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO patient_entity (id, profile_id, medical_record_number, notes, active, created_at, updated_at)
VALUES ('11111111-1111-1111-1111-111111111111', '22222222-2222-2222-2222-222222222222', 'MRN-TEST-002', 'Other test patient', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Doctor entity
INSERT INTO doctor_entity (id, profile_id, doctor_record_number, notes, active, created_at, updated_at)
VALUES ('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'DOC-TEST-001', 'Test doctor', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Patient-Doctor assignment (test patient assigned to test doctor)
INSERT INTO patient_doctor (id, patient_id, doctor_id, assigned_at)
VALUES ('ffffffff-ffff-ffff-ffff-ffffffffffff', 'dddddddd-dddd-dddd-dddd-dddddddddddd', 'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', CURRENT_TIMESTAMP);
