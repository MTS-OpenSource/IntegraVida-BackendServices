BEGIN;

-- Full reset for a local development database.
-- This removes every table in the public schema and lets Hibernate recreate the rest on startup.
DROP SCHEMA IF EXISTS public CASCADE;
CREATE SCHEMA public;

GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO public;

-- Needed only if you want DB-side UUID generation later.
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- Profiles context
CREATE TABLE profiles (
    id UUID PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone_number VARCHAR(255) NOT NULL,
    date_of_birth DATE NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- Patients context. Hibernate will use this exact table name for PatientEntity.
CREATE TABLE patient_entity (
    id UUID PRIMARY KEY,
    profile_id UUID NOT NULL UNIQUE,
    medical_record_number VARCHAR(255) NOT NULL UNIQUE,
    notes TEXT NOT NULL,
    active BOOLEAN NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE treatment_entity (
    id UUID PRIMARY KEY,
    patient_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NULL,
    status VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE medication_entity (
    id UUID PRIMARY KEY,
    patient_id UUID NOT NULL,
    treatment_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    dosage VARCHAR(255) NOT NULL,
    days_of_week TEXT NOT NULL,
    dose_times TEXT NOT NULL,
    instructions TEXT NOT NULL,
    active BOOLEAN NOT NULL,
    discontinued_at TIMESTAMP NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE medication_intake_entity (
    id UUID PRIMARY KEY,
    medication_id UUID NOT NULL,
    patient_id UUID NOT NULL,
    taken_at TIMESTAMP NOT NULL,
    notes TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL
);

-- Seed profile referenced by the patient below.
INSERT INTO profiles (
    id, first_name, last_name, email, phone_number, date_of_birth, created_at, updated_at
) VALUES (
    '11111111-1111-1111-1111-111111111111',
    'Ana',
    'Perez',
    'ana.perez@integravida.com',
    '+1-555-0101',
    '1990-06-13',
    NOW(),
    NOW()
);

-- Seed patient UUID you can reuse in Swagger and monitoring.
INSERT INTO patient_entity (
    id, profile_id, medical_record_number, notes, active, created_at, updated_at
) VALUES (
    '22222222-2222-2222-2222-222222222222',
    '11111111-1111-1111-1111-111111111111',
    'MRN-000001',
    'Seed patient for development',
    TRUE,
    NOW(),
    NOW()
);

INSERT INTO treatment_entity (
    id, patient_id, name, description, start_date, end_date, status, created_at, updated_at
) VALUES (
    '33333333-3333-3333-3333-333333333333',
    '22222222-2222-2222-2222-222222222222',
    'Metformin plan',
    'Daily glucose control plan',
    '2026-06-01',
    '2026-12-01',
    'ACTIVE',
    NOW(),
    NOW()
);

INSERT INTO medication_entity (
    id, patient_id, treatment_id, name, dosage, days_of_week, dose_times, instructions, active, discontinued_at, created_at, updated_at
) VALUES (
    '44444444-4444-4444-4444-444444444444',
    '22222222-2222-2222-2222-222222222222',
    '33333333-3333-3333-3333-333333333333',
    'Metformin',
    '500 mg',
    'MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY',
    '08:00,20:00',
    'Take with meals',
    TRUE,
    NULL,
    NOW(),
    NOW()
);

INSERT INTO medication_intake_entity (
    id, medication_id, patient_id, taken_at, notes, created_at
) VALUES (
    '55555555-5555-5555-5555-555555555555',
    '44444444-4444-4444-4444-444444444444',
    '22222222-2222-2222-2222-222222222222',
    NOW(),
    'Seed medication intake for development',
    NOW()
);

COMMIT;
