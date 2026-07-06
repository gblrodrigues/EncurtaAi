# EncurtaAi

[![Kotlin](https://img.shields.io/badge/kotlin-1.9.25-7F52FF.svg?logo=kotlin)](http://kotlinlang.org)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5.16-6DB33F.svg?logo=springboot)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17-4169E1.svg?logo=postgresql&logoColor=white)](https://www.postgresql.org/)

EncurtaAi is a RESTful URL shortening API built with Kotlin, Spring Boot and PostgreSQL. It allows users to create, 
customize, manage and redirect shortened URLs while tracking access statistics shortened URLs, track access statistics, 
and follows common backend development practices such as layered architecture, input validation, global exception handling, 
database migrations and OpenAPI documentation.

* [Technologies Used](#technologies-used)
* [Features](#features)
* [Running the Project](#running-the-project)
* [API Documentation](#api-documentation)
* [API Endpoints](#api-endpoints)
* [Example Requests and Responses](#example-requests-and-responses)
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
Containers | [![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)](https://www.docker.com/) | Local PostgreSQL environment

## Features

* Create shortened URLs with optional custom short codes
* Redirect using generated or custom short codes
* List links with pagination
* Sort links by creation date
* Track access count and last access timestamp
* Retrieve links by short code
* Update existing links
* Delete links
* Automatic unique short code generation
* Request validation
* Standardized error responses
* OpenAPI documentation with Swagger UI
* Database migrations with Flyway

## Running the Project

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

```text
http://localhost:8080/swagger
```

## API Endpoints

| Method | Endpoint | Description |
|----------|------------|-----|
POST | `/api/v1/links` | Create a shortened URL
GET | `/api/v1/links` | List all links (paginated)
GET | `/api/v1/links/{code}` | Retrieve a link by short code
PUT | `/api/v1/links/{code}` | Update a link
DELETE | `/api/v1/links/{code}` | Delete a link
GET | `/{code}` | Redirect to the original URL

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
  "shortCode": "Ab3Lt9",
  "shortUrl": "http://localhost:8080/Ab3Lt9",
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
      "shortUrl": "http://localhost:8080/github",
      "accessCount": 26,
      "lastAccessedAt": "2026-07-05T15:04:51.548475Z"
    },
    {
      "id": 1,
      "originalUrl": "https://developer.android.com",
      "shortCode": "devandroid",
      "shortUrl": "http://localhost:8080/devandroid",
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

## Project Structure

```text
src/main/kotlin/com/gblrod/encurtaai
│
├── config
│   ├── AppProperties.kt
│   ├── OpenApiConfig.kt
│   ├── PaginationProperties.kt
│
├── controller
│   ├── LinkController.kt
│   ├── RedirectController.kt
│
├── dto
│   ├── CreateLinkRequestDto.kt
│   ├── ErrorResponseDto.kt
│   ├── LinkResponseDto.kt
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
├── service
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
Controllers
   ↓
Services
   ↓
Repositories (Spring Data JPA)
   ↓
PostgreSQL

Validation: Bean Validation
Documentation: OpenAPI / Swagger
Database migrations: Flyway
```

## My Contact

🔗 [LinkedIn](https://www.linkedin.com/in/gblrodrigues/)
