# IntegraVida Backend Services

![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.6-green?logo=spring)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue?logo=postgresql)
![Docker](https://img.shields.io/badge/Docker-Supported-blue?logo=docker)
![Maven](https://img.shields.io/badge/Maven-3.9-red?logo=apache-maven)
![License](https://img.shields.io/badge/License-Open%20Source-green)

**Servicios Backend empresariales para la plataforma IntegraVida**, desarrollados con Spring Boot 4.0.6 y Java 21. Arquitectura modular basada en Domain-Driven Design (DDD) con soporte completo para PostgreSQL, validación de datos y documentación automática.

🌐 **Demo en vivo**: [IntegraVida Backend Services](https://integravida-backendservices.onrender.com)

---

## 📋 Tabla de Contenidos

- [Características](#-características)
- [Requisitos Previos](#-requisitos-previos)
- [Instalación](#-instalación)
- [Configuración](#-configuración)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [API Documentation](#-api-documentation)
- [Operaciones Comunes](#-operaciones-comunes)
- [Docker](#-docker)
- [Troubleshooting](#-troubleshooting)
- [Contributing](#-contributing)

---

## ✨ Características

- ✅ **Spring Boot 4.0.6** - Framework moderno con últimas características
- ✅ **Java 21** - Soporte de características modernas del lenguaje
- ✅ **Spring Data JPA** - Acceso a datos eficiente con ORM
- ✅ **PostgreSQL 16** - Base de datos relacional robusta
- ✅ **Swagger/OpenAPI 3.0** - Documentación automática e interactiva
- ✅ **Validación de Datos** - Validación integral con Spring Validation
- ✅ **Lombok** - Reducción de código boilerplate
- ✅ **Docker & Docker Compose** - Deployment containerizado
- ✅ **Domain-Driven Design** - Arquitectura escalable y mantenible
- ✅ **Spring DevTools** - Hot reload durante desarrollo

---

## 🛠️ Requisitos Previos

### Desarrollo Local

- **Java 21** o superior
- **Maven 3.9** o superior
- **PostgreSQL 16** (o usar Docker)
- **Git**

### Verificar Instalación

```bash
java -version       # Java 21+
mvn -version        # Maven 3.9+
psql --version      # PostgreSQL 16+
```

---

## 🚀 Instalación

### 1. Clonar el Repositorio

```bash
git clone https://github.com/MTS-OpenSource/IntegraVida-BackendServices.git
cd IntegraVida-BackendServices
```

### 2. Configurar Base de Datos

Primero, configura la base de datos PostgreSQL. Consulta el repositorio de base de datos:

📦 **[IntegraVida Database PostgreSQL](https://github.com/MTS-OpenSource/IntegraVida-Database-PostgreSQL)**

```bash
# Levantar contenedores de PostgreSQL
cd ../IntegraVida-Database-PostgreSQL
docker compose up -d
cd ../IntegraVida-BackendServices
```

### 3. Configurar Variables de Entorno

Crea un archivo `.env` en la raíz del proyecto:

```env
# Base de Datos
DATABASE_URL=jdbc:postgresql://localhost:5433/IntegraVida
DATABASE_USERNAME=postgresql
DATABASE_PASSWORD=302511

# Puerto
PORT=8096

# Entorno
SPRING_PROFILES_ACTIVE=dev
```

O modifica `src/main/resources/application.properties` directamente:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5433/IntegraVida
spring.datasource.username=postgresql
spring.datasource.password=302511
server.port=8096
```

### 4. Compilar el Proyecto

```bash
# Descargar dependencias y compilar
mvn clean install

# O solo compilar sin tests
mvn clean install -DskipTests
```

### 5. Ejecutar la Aplicación

```bash
# Opción 1: Desde Maven
mvn spring-boot:run

# Opción 2: Ejecutar JAR compilado
java -jar target/IntegraVidaBackend-0.0.1-SNAPSHOT.jar
```

La aplicación estará disponible en: `http://localhost:8096`

---

## ⚙️ Configuración

### Archivos de Configuración

| Archivo | Propósito |
|---------|----------|
| `pom.xml` | Dependencias Maven y versiones |
| `application.properties` | Configuración de Spring Boot |
| `Dockerfile` | Imagen Docker para producción |
| `docker-compose.yml` | Orquestación de servicios |

### Propiedades de Aplicación

```properties
# Aplicación
spring.application.name=IntegraVidaBackend
server.port=8096

# Base de Datos
spring.datasource.url=jdbc:postgresql://localhost:5433/IntegraVida
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.database=postgresql
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Validación
spring.jpa.open-in-view=true
```

### Perfiles de Ejecución

Ejecutar con un perfil específico:

```bash
# Desarrollo
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"

# Producción
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=prod"
```

---

## 📁 Estructura del Proyecto

```
IntegraVida-BackendServices/
├── src/
│   ├── main/
│   │   ├── java/com/integravida/IntegraVidaBackend/
│   │   │   ├── iam/                    # Identity & Access Management
│   │   │   │   ├── domain/
│   │   │   │   ├── application/
│   │   │   │   ├── infrastructure/
│   │   │   │   └── ...
│   │   │   └── [otros bounded contexts]
│   │   └── resources/
│   │       ├── application.properties
│   │       └── application-{profile}.properties
│   └── test/                           # Tests unitarios e integración
├── pom.xml                             # Gestión de dependencias
├── Dockerfile                          # Imagen Docker multi-stage
├── docker-compose.yml                  # Orquestación local
└── README.md                           # Este archivo
```

### Arquitectura DDD (Domain-Driven Design)

El proyecto está organizado por **Bounded Contexts**:

- **IAM (Identity & Access Management)** - Autenticación y autorización
- **Otros Bounded Contexts** - Diferentes dominios de negocio

Cada Bounded Context sigue la estructura:
```
context/
├── domain/           # Entities, Value Objects, Repositories
├── application/      # Use Cases, Services
├── infrastructure/   # Implementaciones, Adapters
└── presentation/     # Controllers, DTOs
```

---

## 📚 API Documentation

### Swagger UI

Accede a la documentación interactiva de la API:

🔗 **[Swagger UI](http://localhost:8096/swagger-ui/index.html)**

```
http://localhost:8096/swagger-ui/index.html
```

### OpenAPI JSON

Descarga la especificación OpenAPI en formato JSON:

```
http://localhost:8096/v3/api-docs
```

### Documentación Genérica

```
http://localhost:8096/v3/api-docs.yaml
```

---

## 🔧 Operaciones Comunes

### Compilar y Ejecutar Tests

```bash
# Ejecutar todos los tests
mvn test

# Ejecutar un test específico
mvn test -Dtest=NombreDelTest

# Ejecutar tests con cobertura
mvn clean test jacoco:report
```

### Limpiar y Reconstruir

```bash
# Limpiar artifacts compilados
mvn clean

# Reconstruir proyecto
mvn clean install

# Reconstruir sin ejecutar tests
mvn clean install -DskipTests
```

### Ver Logs

```bash
# Ver logs en tiempo real
tail -f logs/application.log

# Ver logs filtrando por nivel
grep "ERROR" logs/application.log
```

### Acceder a la Base de Datos

```bash
# Conectarse a PostgreSQL desde la CLI
docker exec -it postgres-container psql -U postgresql -d IntegraVida

# O usar pgAdmin (si está configurado)
# http://localhost:5050
```

---

## 🐳 Docker

### Construir la Imagen Docker

```bash
# Construir imagen
docker build -t integravida-backend:latest .

# Construir con etiqueta personalizada
docker build -t integravida-backend:v1.0.0 .
```

### Ejecutar en Docker

```bash
# Ejecutar contenedor
docker run -d \
  --name integravida-backend \
  -p 8096:8096 \
  -e DATABASE_URL=jdbc:postgresql://postgres:5433/IntegraVida \
  -e DATABASE_USERNAME=postgresql \
  -e DATABASE_PASSWORD=302511 \
  integravida-backend:latest

# Ver logs del contenedor
docker logs -f integravida-backend

# Detener contenedor
docker stop integravida-backend

# Eliminar contenedor
docker rm integravida-backend
```

### Docker Compose

```bash
# Levantar servicios (backend + database)
docker compose up -d

# Ver estado de servicios
docker compose ps

# Ver logs
docker compose logs -f backend

# Detener servicios
docker compose down

# Reconstruir imagen
docker compose up -d --build
```

---

## 🐛 Troubleshooting

### Error: "Connection refused" en PostgreSQL

**Causa**: La base de datos no está corriendo

**Solución**:
```bash
# Verificar que PostgreSQL esté ejecutándose
docker ps

# Iniciar PostgreSQL si no está corriendo
docker compose up -d postgres
```

### Error: "Port 8096 already in use"

**Causa**: Otro proceso está usando el puerto

**Solución**:
```bash
# Cambiar puerto en application.properties
server.port=8097

# O matar el proceso que usa el puerto
lsof -ti:8096 | xargs kill -9
```

### Error: "Maven compilation failed"

**Causa**: Problemas con dependencias o versión de Java

**Solución**:
```bash
# Limpiar cache de Maven
mvn clean

# Verificar versión de Java
java -version  # Debe ser 21+

# Reinstalar dependencias
mvn dependency:resolve -U
```

### Error: "Database initialization failed"

**Causa**: Script SQL no ejecutado correctamente

**Solución**:
1. Verifica que `GeneralQuery.sql` existe en la DB
2. Reinicia el contenedor de PostgreSQL
3. Revisa los logs: `docker compose logs postgres`

### Hot Reload no funciona

**Causa**: Spring DevTools no está activado correctamente

**Solución**:
```bash
# Reconstruir proyecto
mvn clean install

# Ejecutar con Spring Boot plugin
mvn spring-boot:run
```

### Swagger UI no accesible

**Causa**: Aplicación no está corriendo o documentación deshabilitada

**Solución**:
```bash
# Verificar que app está corriendo
curl http://localhost:8096/actuator/health

# Acceder a Swagger
http://localhost:8096/swagger-ui/index.html
```

---

## 📊 Métricas y Monitoreo

### Health Check

```bash
curl http://localhost:8096/actuator/health
```

### Información de la Aplicación

```bash
curl http://localhost:8096/actuator/info
```

### Métricas

```bash
curl http://localhost:8096/actuator/metrics
```

---

## 🔐 Seguridad

⚠️ **IMPORTANTE**: Las credenciales mostradas en este documento son para desarrollo local únicamente.

### Para Producción

1. **Nunca** commits credenciales al repositorio
2. Usa **variables de entorno** o **secrets management**
3. Implementa **Spring Security** con autenticación robusta
4. Habilita **HTTPS** en producción
5. Usa **JWT** o **OAuth2** para APIs
6. Valida todas las entradas de usuario
7. Implementa **rate limiting**

---

## 📝 Control de Versiones de Tareas

### Registrar progreso en Linear

Una vez completado tu **Bounded Context**, asegúrate de:

1. ✅ Completar todos los tickets en **LINEAR**
2. ✅ Documentar cambios en PRs
3. ✅ Ejecutar tests unitarios
4. ✅ Actualizar documentación

⚠️ **Nota**: Sin registrar en Linear, tu progreso no se reflejará en el backlog ni sprint.

**Linear Dashboard**: [Ver tareas](https://linear.app) (acceso requerido)

---

## 🤝 Contributing

Las contribuciones son bienvenidas. Por favor sigue estos pasos:

### 1. Fork el Repositorio

```bash
git clone https://github.com/MTS-OpenSource/IntegraVida-BackendServices.git
cd IntegraVida-BackendServices
```

### 2. Crear rama de Feature

```bash
# Usar nomenclatura descriptiva
git checkout -b feature/bounded-context-feature
# o
git checkout -b fix/bug-description
# o
git checkout -b docs/documentation-update
```

### 3. Hacer Cambios

- Escribir código limpio y documentado
- Seguir convenciones de Java
- Incluir tests unitarios
- Actualizar documentación

### 4. Commit y Push

```bash
# Commits descriptivos siguiendo convención
git commit -m "feat: agregar nueva funcionalidad en IAM"
git commit -m "fix: corregir bug en validación"
git commit -m "docs: actualizar README"

# Push a tu rama
git push origin feature/bounded-context-feature
```

### 5. Crear Pull Request

1. Ve a GitHub y abre un Pull Request
2. Describe los cambios realizados
3. Referencia issues relacionados
4. Espera revisión y feedback

### Convenciones de Código

- **Nombre de clases**: PascalCase
- **Nombre de métodos**: camelCase
- **Constantes**: UPPER_SNAKE_CASE
- **Paquetes**: lowercase
- **Documentación**: JavaDoc en métodos públicos

---

## 📚 Recursos Adicionales

### Documentación Oficial
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/16/)
- [Swagger/OpenAPI 3.0](https://swagger.io/specification/)

### Guías de Desarrollo
- [Spring Boot Best Practices](https://spring.io/guides)
- [Domain-Driven Design](https://martinfowler.com/bliki/DomainDrivenDesign.html)
- [Java 21 Features](https://docs.oracle.com/en/java/javase/21/)

### Herramientas Recomendadas
- **IDE**: IntelliJ IDEA / VS Code + Extension Pack for Java
- **Cliente HTTP**: Postman / Insomnia / Thunder Client
- **BD GUI**: pgAdmin / DBeaver
- **Versionado**: Git / GitHub Desktop

---

## 📞 Soporte y Contacto

### Reportar Issues

Abre un [issue en GitHub](https://github.com/MTS-OpenSource/IntegraVida-BackendServices/issues) con:
- Descripción clara del problema
- Pasos para reproducir
- Logs o screenshots relevantes
- Versiones de Java, Maven, PostgreSQL

### Discusiones y Preguntas

Usa las [GitHub Discussions](https://github.com/MTS-OpenSource/IntegraVida-BackendServices/discussions) para:
- Preguntas sobre arquitectura
- Sugerencias de mejora
- Compartir conocimiento

---

## 📄 Licencia

Este proyecto es parte de **MTS-OpenSource**. Consulta el archivo LICENSE para más detalles.

---

**Última actualización**: Junio 2026  
**Estado**: En desarrollo activo ✅  
**Versión**: 0.0.1-SNAPSHOT  
**Java**: 21 | **Spring Boot**: 4.0.6 | **PostgreSQL**: 16
