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

Estructura de paquetes (igual que ACME)
com.integravida.platform/
├── iam/
│   ├── interfaces/rest/
│   ├── interfaces/acl/            ← IamContextFacade
│   ├── application/commandservices/
│   ├── application/queryservices/
│   ├── application/internal/commandservices/
│   ├── application/internal/queryservices/
│   ├── application/internal/outboundservices/hashing/
│   ├── application/internal/outboundservices/tokens/
│   ├── domain/model/aggregates/
│   ├── domain/model/entities/
│   ├── domain/model/valueobjects/
│   ├── domain/repositories/
│   └── infrastructure/
│
├── profiles/
│   ├── interfaces/rest/
│   ├── interfaces/acl/            ← ProfilesContextFacade
│   ├── interfaces/events/         ← ProfileCreatedIntegrationEvent
│   ├── application/commandservices/
│   ├── application/queryservices/
│   ├── application/internal/commandservices/
│   ├── application/internal/queryservices/
│   ├── application/internal/eventhandlers/
│   ├── application/acl/           ← ProfilesContextFacadeImpl
│   ├── domain/model/aggregates/   ← Profile
│   ├── domain/model/valueobjects/
│   ├── domain/model/events/       ← ProfileCreatedEvent
│   ├── domain/repositories/
│   └── infrastructure/
│
├── patients/
│   ├── interfaces/rest/
│   ├── application/commandservices/
│   ├── application/queryservices/
│   ├── application/internal/commandservices/
│   ├── application/internal/queryservices/
│   ├── application/internal/outboundservices/acl/ ← ExternalProfileService
│   ├── application/internal/eventhandlers/        ← escucha ProfileCreatedIntegrationEvent
│   ├── domain/model/aggregates/   ← Patient, Treatment, Medication
│   ├── domain/model/entities/
│   ├── domain/model/valueobjects/
│   ├── domain/repositories/
│   └── infrastructure/
│
├── monitoring/
│   ├── interfaces/rest/
│   ├── application/commandservices/
│   ├── application/queryservices/
│   ├── application/internal/commandservices/
│   ├── application/internal/queryservices/
│   ├── application/internal/outboundservices/acl/ ← ExternalPatientService
│   ├── domain/model/aggregates/   ← GlucoseRecord, Alert, ClinicalObservation
│   ├── domain/model/valueobjects/ ← GlucoseValue, AlertSeverity, GlucoseRange
│   ├── domain/model/events/       ← GlucoseAlertTriggeredEvent
│   ├── domain/repositories/
│   └── infrastructure/
│
├── medical/
│   ├── interfaces/rest/
│   ├── application/commandservices/
│   ├── application/queryservices/
│   ├── application/internal/commandservices/
│   ├── application/internal/queryservices/
│   ├── application/internal/outboundservices/acl/ ← ExternalPatientService, ExternalMonitoringService
│   ├── domain/model/aggregates/   ← Diagnosis, ClinicalReport, Appointment
│   ├── domain/model/valueobjects/
│   ├── domain/repositories/
│   └── infrastructure/
│
└── shared/
    ├── domain/model/aggregates/   ← AbstractDomainAggregateRoot
    ├── application/result/        ← Result<T,E>, ApplicationError
    ├── interfaces/rest/           ← GlobalExceptionHandler, ErrorResource
    └── infrastructure/            ← OpenAPI config, i18n

Relaciones entre contextos (igual que ACME usa ACL Facades)
profiles  ──publica──▶  ProfileCreatedIntegrationEvent
                              │
               ┌──────────────┘
               ▼
patients  (LearningProfileCreatedEventHandler equivalente)
               │
               └──▶ ExternalProfileService ──▶ ProfilesContextFacade

monitoring ──▶ ExternalPatientService ──▶ (patients interno)

medical    ──▶ ExternalPatientService    ──▶ (patients interno)
           ──▶ ExternalMonitoringService ──▶ (monitoring interno)
La regla clave del patrón: ningún contexto importa clases de otro directamente, siempre pasan por un Facade (ACL) o por eventos de integración.
