1. iam — Identity and Access Management
Igual que en ACME. Maneja autenticación y roles.
Aggregates: User
Entities: Role
Value Objects: Roles (enum: Patient, Doctor)
Servicios: UserCommandService, UserQueryService, RoleQueryService
ACL Facade: IamContextFacade
Endpoints: POST /api/v1/authentication/sign-in, sign-up, /users, /roles

2. profiles — Perfil de Usuario
Gestiona los datos personales del usuario (nombre, email, datos clínicos básicos).
Aggregates: Profile
Value Objects: PersonName, EmailAddress, DateOfBirth, PhoneNumber
Servicios: ProfileCommandService, ProfileQueryService
ACL Facade: ProfilesContextFacade (lo consume patients y doctors)
Endpoints: /api/v1/profiles
Eventos: publica ProfileCreatedEvent → lo escucha patients y doctors

3. patients — Gestión de Pacientes
Registra al paciente, sus tratamientos y medicaciones.
Aggregates: Patient, Treatment, Medication
Value Objects: PatientRecordId, ProfileId, TreatmentStatus, MedicationSchedule
Entities: MedicationIntake
Servicios: PatientCommandService, PatientQueryService, TreatmentCommandService, TreatmentQueryService, MedicationCommandService, MedicationQueryService
Outbound ACL: ExternalProfileService (consulta ProfilesContextFacade)
Endpoints: /api/v1/patients, /api/v1/treatments, /api/v1/medications, /api/v1/medication-intakes
Evento recibido: ProfileCreatedEvent → crea Patient automáticamente

4. monitoring — Monitoreo de Glucosa y Alertas
El core clínico: registros de glucosa, alertas automáticas, rangos y observaciones.
Aggregates: GlucoseRecord, Alert, ClinicalObservation
Value Objects: GlucoseValue (con lógica de rango), AlertSeverity (enum: low, medium, high, critical), GlucoseRange, PatientId
Servicios: GlucoseRecordCommandService, GlucoseRecordQueryService, AlertCommandService, AlertQueryService
Lógica de dominio: al crear un GlucoseRecord, el aggregate evalúa el valor contra GlucoseRange y publica GlucoseAlertTriggeredEvent si corresponde
Outbound ACL: ExternalPatientService (verifica que el paciente exista)
Endpoints: /api/v1/glucose-records, /api/v1/alerts, /api/v1/glucose-ranges, /api/v1/clinical-observations

5. medical — Seguimiento Médico
Dashboard del médico: diagnósticos, reportes clínicos y citas.
Aggregates: Diagnosis, ClinicalReport, Appointment
Value Objects: DoctorId, PatientId, AppointmentStatus (enum), DiagnosisCode
Servicios: DiagnosisCommandService, DiagnosisQueryService, AppointmentCommandService, AppointmentQueryService, ClinicalReportCommandService, ClinicalReportQueryService
Outbound ACL: ExternalPatientService, ExternalMonitoringService (lee glucosa y alertas para armar reportes)
Endpoints: /api/v1/diagnoses, /api/v1/clinical-reports, /api/v1/appointments
