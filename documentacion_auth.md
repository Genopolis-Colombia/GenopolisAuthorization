# Documentación del Microservicio de Autenticación — GenopolisAuth

## 1. Introducción

**GenopolisAuth** es el microservicio de autenticación y gestión de identidad de la plataforma Genopolis Colombia, un sistema de gestión de información biotecnológica orientado al trabajo con datos de proteínas e investigación colaborativa.

Este microservicio es responsable de:

- Registrar nuevos usuarios en el sistema.
- Autenticar usuarios mediante credenciales (usuario/contraseña).
- Emitir y validar tokens de acceso JWT.
- Permitir la actualización y eliminación de cuentas de usuario.

Actúa como puerta de entrada para todos los servicios de la plataforma Genopolis que requieren identidad verificada. Los demás microservicios pueden delegar la validación de tokens a este servicio.

---

## 2. Descripción General de la API

| Atributo | Valor |
|---|---|
| **Puerto** | `8081` |
| **URL base** | `http://localhost:8081/auth` |
| **Protocolo** | HTTP/1.1 |
| **Formato de datos** | JSON (`application/json`) |
| **Autenticación** | JWT (Bearer Token — HMAC256/HMAC512) |
| **CORS permitido** | `http://localhost:5173` |

### Tecnologías principales

| Tecnología | Versión | Rol |
|---|---|---|
| Java | 17 | Lenguaje |
| Spring Boot | 3.2.4 | Framework principal |
| Spring Data JPA | 3.2.4 | Acceso a base de datos |
| Spring Security Crypto | 3.2.4 | Hashing de contraseñas (BCrypt) |
| Auth0 Java JWT | 4.5.0 | Generación y validación de tokens |
| MySQL | 8.x | Base de datos relacional |

### Arquitectura

El proyecto sigue **arquitectura hexagonal (Ports & Adapters)** dividida en dos módulos Gradle:

- **`domain`** — Lógica de negocio pura: entidades, puertos, casos de uso y excepciones. Sin dependencias de framework.
- **`infra`** — Implementaciones concretas: controladores REST, repositorio MySQL, servicios JWT y BCrypt, configuración Spring.

---

## 3. Modelo de Datos

### Entidad `User`

| Campo | Tipo | Descripción |
|---|---|---|
| `id` | UUID | Identificador único del usuario |
| `username` | String | Nombre de usuario (único) |
| `email` | String | Correo electrónico (único) |
| `passwordHash` | String | Contraseña hasheada con BCrypt |
| `role` | Enum | Rol del usuario: `ADMIN` o `USER` |
| `createdAt` | Timestamp | Fecha de creación (auto-gestionada) |
| `updatedAt` | Timestamp | Fecha de última actualización (auto-gestionada) |

---

## 4. Documentación de Endpoints

### 4.1 Registro de usuario

**`POST /auth/register`**

Crea una nueva cuenta de usuario y retorna un token JWT de acceso.

**Request Body:**
```json
{
  "username": "jdoe",
  "email": "jdoe@genopolis.co",
  "password": "MiContraseña123",
  "role": "USER"
}
```

| Campo | Tipo | Requerido | Descripción |
|---|---|---|---|
| `username` | String | Sí | Nombre de usuario único |
| `email` | String | Sí | Correo electrónico único |
| `password` | String | Sí | Contraseña en texto plano (se almacena hasheada) |
| `role` | String | Sí | Rol del usuario: `USER` o `ADMIN` |

**Respuesta exitosa — `201 Created`:**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Respuestas de error:**

| Código | Motivo |
|---|---|
| `400 Bad Request` | Datos inválidos o rol no reconocido |
| `500 Internal Server Error` | Error interno del servidor |

---

### 4.2 Inicio de sesión

**`POST /auth/login`**

Autentica un usuario existente y retorna un token JWT junto con su tiempo de expiración.

**Request Body:**
```json
{
  "username": "jdoe",
  "password": "MiContraseña123"
}
```

| Campo | Tipo | Requerido | Descripción |
|---|---|---|---|
| `username` | String | Sí | Nombre de usuario |
| `password` | String | Sí | Contraseña en texto plano |

**Respuesta exitosa — `200 OK`:**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "expiresIn": 3600
}
```

| Campo | Descripción |
|---|---|
| `accessToken` | Token JWT firmado |
| `expiresIn` | Tiempo de expiración en segundos (por defecto 3600) |

**Respuestas de error:**

| Código | Motivo |
|---|---|
| `401 Unauthorized` | Credenciales incorrectas |
| `404 Not Found` | Usuario no encontrado |
| `500 Internal Server Error` | Error interno del servidor |

---

### 4.3 Validación de token

**`POST /auth/validate`**

Verifica que un token JWT sea válido y retorna la información del usuario asociado.

**Request Body:**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

| Campo | Tipo | Requerido | Descripción |
|---|---|---|---|
| `accessToken` | String | Sí | Token JWT a validar |

**Respuesta exitosa — `200 OK`:**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "username": "jdoe",
  "email": "jdoe@genopolis.co",
  "role": "USER"
}
```

**Respuestas de error:**

| Código | Motivo |
|---|---|
| `401 Unauthorized` | Token inválido o expirado |
| `404 Not Found` | Usuario del token no encontrado |
| `500 Internal Server Error` | Error interno del servidor |

---

### 4.4 Actualización de usuario

**`POST /auth/update`**

Actualiza los datos del usuario identificado por el token. Al menos uno de los campos opcionales debe ser proporcionado.

**Request Body:**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "nuevo_usuario",
  "email": "nuevo@genopolis.co",
  "password": "NuevaContraseña456"
}
```

| Campo | Tipo | Requerido | Descripción |
|---|---|---|---|
| `accessToken` | String | Sí | Token JWT del usuario autenticado |
| `username` | String | No | Nuevo nombre de usuario |
| `email` | String | No | Nuevo correo electrónico |
| `password` | String | No | Nueva contraseña en texto plano |

**Respuesta exitosa — `204 No Content`** (sin cuerpo de respuesta)

**Respuestas de error:**

| Código | Motivo |
|---|---|
| `400 Bad Request` | No se proporcionó ningún campo a actualizar |
| `401 Unauthorized` | Token inválido o expirado |
| `404 Not Found` | Usuario no encontrado |
| `500 Internal Server Error` | Error interno del servidor |

---

### 4.5 Eliminación de usuario

**`POST /auth/delete`**

Elimina permanentemente la cuenta del usuario identificado por el token.

**Request Body:**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

| Campo | Tipo | Requerido | Descripción |
|---|---|---|---|
| `accessToken` | String | Sí | Token JWT del usuario autenticado |

**Respuesta exitosa — `204 No Content`** (sin cuerpo de respuesta)

**Respuestas de error:**

| Código | Motivo |
|---|---|
| `401 Unauthorized` | Token inválido o expirado |
| `404 Not Found` | Usuario no encontrado |
| `500 Internal Server Error` | Error interno del servidor |

---

## 5. Estructura del Token JWT

Los tokens emitidos por este servicio tienen la siguiente estructura de claims:

| Claim | Descripción |
|---|---|
| `sub` | ID del usuario (UUID) |
| `iss` | Emisor: `genopolis-auth-service` |
| `aud` | Audiencia: `genopolis-services` |
| `iat` | Fecha de emisión (Unix timestamp) |
| `exp` | Fecha de expiración (Unix timestamp) |
| `username` | Nombre de usuario |
| `role` | Rol del usuario (`ADMIN` o `USER`) |

**Ejemplo de payload decodificado:**
```json
{
  "sub": "550e8400-e29b-41d4-a716-446655440000",
  "iss": "genopolis-auth-service",
  "aud": "genopolis-services",
  "iat": 1710800000,
  "exp": 1710803600,
  "username": "jdoe",
  "role": "USER"
}
```

---

## 6. Formato de Respuesta de Error

Todos los errores retornan el siguiente formato:

```json
{
  "failure": "TIPO_DE_ERROR",
  "detail": "Descripción detallada del error"
}
```

| Campo | Descripción |
|---|---|
| `failure` | Código o tipo de error |
| `detail` | Mensaje descriptivo del error |

---

## 7. Configuración

Las siguientes propiedades pueden ser sobreescritas mediante variables de entorno:

| Propiedad | Variable de entorno | Valor por defecto |
|---|---|---|
| JWT Secret | `GENOPOLIS_JWT_SECRET` | `super_secreto_genopolis` |
| JWT Issuer | `GENOPOLIS_JWT_ISSUER` | `genopolis-auth-service` |
| JWT Audience | `GENOPOLIS_JWT_AUDIENCE` | `genopolis-services` |
| JWT Algoritmo | `GENOPOLIS_JWT_ALGORITHM` | `HMAC256` |
| Expiración access token | `GENOPOLIS_JWT_ACCESS_EXPIRATION` | `3600` (segundos) |
| Expiración refresh token | `GENOPOLIS_JWT_REFRESH_EXPIRATION` | `604800` (segundos) |

> **Importante:** En entornos de producción es obligatorio sobreescribir `GENOPOLIS_JWT_SECRET` con un valor seguro y aleatorio.

---

## 8. Configuración de la Base de Datos

| Parámetro | Valor |
|---|---|
| Motor | MySQL 8.x |
| Base de datos | `authorization_genopolis` |
| Host | `127.0.0.1:3306` |
| Usuario | `springuser` |
| DDL | `update` (Hibernate gestiona el esquema) |

**Script de inicialización MySQL:**
```sql
CREATE DATABASE authorization_genopolis;
CREATE USER 'springuser'@'%' IDENTIFIED BY 'p4t1App';
GRANT ALL ON authorization_genopolis.* TO 'springuser'@'%';
```

---

## 9. Ejecución del Servicio

**Requisitos previos:**
- Java 17
- MySQL 8.x corriendo localmente
- Docker (solo para pruebas de integración)

**Compilar:**
```bash
./gradlew build
```

**Ejecutar:**
```bash
./gradlew bootRun
```

**Ejecutar pruebas:**
```bash
./gradlew test
```

El servicio quedará disponible en `http://localhost:8081`.

---

## 10. Resumen de Endpoints

| Método | Endpoint | Descripción | Autenticación requerida |
|---|---|---|---|
| `POST` | `/auth/register` | Registro de nuevo usuario | No |
| `POST` | `/auth/login` | Inicio de sesión | No |
| `POST` | `/auth/validate` | Validación de token JWT | No (el token va en el body) |
| `POST` | `/auth/update` | Actualización de datos del usuario | Sí (token en body) |
| `POST` | `/auth/delete` | Eliminación de cuenta | Sí (token en body) |
