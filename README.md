# EncurtaAi

[![Kotlin](https://img.shields.io/badge/kotlin-1.9.25-7F52FF.svg?logo=kotlin)](http://kotlinlang.org)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5.16-6DB33F.svg?logo=springboot)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17-4169E1.svg?logo=postgresql&logoColor=white)](https://www.postgresql.org/)

EncurtaAi is a RESTful URL shortening API built with Kotlin, Spring Boot and PostgreSQL. It allows administrators to create, customize and 
manage shortened URLs through JWT authentication while providing public access to redirects and link queries.

* [Technologies Used](#technologies-used)
* [Live API](#live-api)
* [Features](#features)
* [Running Locally](#running-locally)
* [API Documentation](#api-documentation)
* [API Endpoints](#api-endpoints)
* [Example Requests and Responses](#example-requests-and-responses)
* [Authentication](#authentication)
* [Project Structure](#project-structure)
* [Architecture](#architecture)
* [My Contact](#my-contact)

## Technologies Used

| Category | Technology | Why |
|----------|------------|-----|
Language | [![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org/) | Main development language (my favorite 💜)
Framework | [![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot) | REST API framework
Persistence | [![Spring Data JPA](https://img.shields.io/badge/Spring_Data_JPA-6DB33F?style=for-the-badge&logo=spring&logoColor=white)](https://spring.io/projects/spring-data-jpa) | ORM and database access
Database | [![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)](https://www.postgresql.org/) | Persistent data storage
Database Migrations | [![Flyway](https://img.shields.io/badge/Flyway-CC0200?style=for-the-badge&logo=flyway&logoColor=white)](https://documentation.red-gate.com/flyway/) | Versioned database schema
Validation | [![Bean Validation](https://img.shields.io/badge/Bean_Validation-2496ED?style=for-the-badge)](https://beanvalidation.org/) | Request validation |
Documentation UI | [![Swagger UI](https://img.shields.io/badge/Swagger_UI-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)](https://swagger.io/tools/swagger-ui/) | Interactive API documentation
Build System | [![Gradle Kotlin DSL](https://img.shields.io/badge/Gradle-Kotlin_DSL-02303A?style=for-the-badge&logo=gradle&logoColor=white)](https://gradle.org/) | Build automation
Containers | [![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)](https://www.docker.com/) | Containerized application and local development
Deployment | [![Render](https://img.shields.io/badge/Render-black?style=for-the-badge&logo=render&logoColor=46E3B7)](https://render.com) | Cloud hosting and application deployment

## Live API
The API is publicly available on Render.

**Base URL**

```text
https://encurtaai-7g31.onrender.com
```

**Swagger**

```text
https://encurtaai-7g31.onrender.com/swagger
```

## Features

* JWT-based authentication for protected operations
* Admin-only access for creating, updating and deleting links
* Public redirect access using generated short codes
* Public link retrieval by short code
* Public paginated link listing
* Custom short codes support
* Automatic unique short code generation
* Access count and last access timestamp tracking
* Request validation
* Standardized error responses
* OpenAPI documentation with Swagger UI
* Database migrations with Flyway

## Running Locally
Copy the environment variables from `.env.example` and configure your local values before starting the application.

```env
ADMIN_USERNAME=admin
ADMIN_PASSWORD_HASH=<bcrypt_hash>
JWT_SECRET=change_this_secret_with_at_least_32_characters
JWT_EXPIRATION=3600000
DATABASE_URL=jdbc:postgresql://localhost:5432/encurtaai
DATABASE_USERNAME=postgres
DATABASE_PASSWORD=postgres
```

Clone the repository:

```bash
git clone https://github.com/gblrodrigues/encurtaai.git
```

Navigate to the project folder:

```bash
cd encurtaai
```

Start PostgreSQL:

```bash
docker compose up -d
```

Run the application:

```bash
./gradlew bootRun
```

> Flyway automatically applies all database migrations during application startup.

The API will be available at:

```text
http://localhost:8080
```

## API Documentation

Swagger UI is available at:

### Local

```text
http://localhost:8080/swagger
```

### Production

```text
https://encurtaai-7g31.onrender.com/swagger
```

## API Endpoints

| Method | Endpoint | Access | Description |
|----------|------------|-----|-----|
POST | `/api/v1/links` | Admin | Create a shortened URL
GET | `/api/v1/links` | Public | List all links
GET | `/api/v1/links/{code}` | Public | Retrieve a link by short code
PUT | `/api/v1/links/{code}` | Admin | Update a link
DELETE | `/api/v1/links/{code}` | Admin | Delete a link
GET | `/{code}` | Public | Redirect to the original URL
POST | `/api/v1/auth/login` | Public | Authenticate administrator and generate JWT token

### Pagination

`GET /api/v1/links` supports the following query parameters:

| Parameter | Default | Description |
|----------|------------|-----|
`page` | `0` | Page index (zero-based)
`size` | `25` | Number of items per page (maximum: 200)
`sort` | `createdAt,desc` | Sort field and direction

## Example Requests and Responses

**POST /api/v1/links**

Request (automatic short code)

```json
{
  "url": "https://developer.android.com"
}
```

Request (custom short code)

```json
{
  "url": "https://developer.android.com",
  "shortCode": "devandroid"
}
```

Response

```json
{
  "id": 1,
  "originalUrl": "https://developer.android.com",
  "shortCode": "devandroid",
  "shortUrl": "https://encurtaai-7g31.onrender.com/devandroid",
  "accessCount": 0,
  "lastAccessedAt": null
}
```

**GET /api/v1/links**
```json
{
  "data": [
    {
      "id": 2,
      "originalUrl": "https://github.com/gblrodrigues",
      "shortCode": "github",
      "shortUrl": "https://encurtaai-7g31.onrender.com/github",
      "accessCount": 26,
      "lastAccessedAt": "2026-07-05T15:04:51.548475Z"
    },
    {
      "id": 1,
      "originalUrl": "https://developer.android.com",
      "shortCode": "devandroid",
      "shortUrl": "https://encurtaai-7g31.onrender.com/devandroid",
      "accessCount": 24,
      "lastAccessedAt": "2026-07-05T15:04:51.548475Z"
    }
  ],
  "pagination": {
    "page": 0,
    "size": 25,
    "totalItems": 2,
    "totalPages": 1
  }
}
```

## Authentication
Protected endpoints require a JWT Bearer token.

First, authenticate using:

**POST /api/v1/auth/login**

Response:

```json
{
  "accessToken": "jwt-token",
  "tokenType": "Bearer",
  "expiresIn": 3600
}
```

## Project Structure

```text
src/main/kotlin/com/gblrod/encurtaai
│
├── config
│   ├── AppProperties.kt
│   ├── JwtProperties.kt
│   ├── OpenApiConfig.kt
│   ├── PaginationProperties.kt
│   ├── PasswordConfig.kt
│   ├── SecurityConfig.kt
│   ├── SecurityProperties.kt
│
├── controller
│   ├── AuthController.kt
│   ├── LinkController.kt
│   ├── RedirectController.kt
│
├── dto
│   ├── CreateLinkRequestDto.kt
│   ├── ErrorResponseDto.kt
│   ├── LinkResponseDto.kt
│   ├── LoginRequestDto.kt
│   ├── LoginResponseDto.kt
│   ├── PageResponseDto.kt
│   ├── PaginationDto.kt
│   ├── UpdateLinkRequestDto.kt
│
├── entity
│   ├── Link.kt
│
├── exception
│   ├── GlobalExceptionHandler.kt
│   ├── LinkNotFoundException.kt
│   ├── ShortCodeAlreadyExistsException.kt
│
├── mapper
│   ├── LinkMapper.kt
│
├── repository
│   ├── LinkRepository.kt
│
├── security
│   ├── JwtAuthenticationFilter.kt
│   ├── JwtService.kt
|
├── service
│   ├── AuthService.kt
│   ├── CodeGenerator.kt
│   ├── LinkService.kt
│
└── EncurtaaiApplication.kt
│
└── resources
    └── db
        └── migration
```

## Architecture

```text
Client
   ↓
JWT Authentication Filter
   ↓
Controllers
   ↓
Services
   ↓
Repositories (Spring Data JPA)
   ↓
PostgreSQL

Validation: Bean Validation
Security: Spring Security + JWT
Documentation: OpenAPI / Swagger
Database migrations: Flyway
```

## My Contact

🔗 [LinkedIn](https://www.linkedin.com/in/gblrodrigues/)
