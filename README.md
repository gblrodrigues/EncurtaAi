# EncurtaAi

![API Version](https://img.shields.io/badge/API-v1.0-blue)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.25-7F52FF)](https://kotlinlang.org/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5.16-6DB33F)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17-4169E1)](https://www.postgresql.org/)

EncurtaAi is a RESTful URL shortening API built with Kotlin, Spring Boot and PostgreSQL. It allows users to create, manage and 
redirect shortened URLs while following common backend development practices such as layered architecture, input validation, 
global exception handling, database migrations and OpenAPI documentation.

* [Technologies Used](#technologies-used)
* [Features](#features)
* [Running the Project](#running-the-project)
* [API Documentation](#api-documentation)
* [API Endpoints](#api-endpoints)
* [Example Request & Response](#example-request-&-Response)
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

* Create shortened URLs
* Redirect using generated or custom short codes
* List all registered links
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
GET | `/api/v1/links` | List all links
GET | `/api/v1/links/{code}` | Retrieve a link by short code
PUT | `/api/v1/links/{code}` | Update a link
DELETE | `/api/v1/links/{code}` | Delete a link
GET | `/{code}` | Redirect to the original URL

## Example Request & Response

**POST /api/v1/links**

Request

```json
{
  "url": "https://developer.android.com"
}
```

Response

```json
{
  "id": 2,
  "originalUrl": "https://developer.android.com",
  "shortCode": "Ab3Lt9",
  "shortUrl": "http://localhost:8080/Ab3Lt9"
}
```

**GET /api/v1/links**

```json
[
  {
    "id": 1,
    "originalUrl": "https://github.com/gblrodrigues",
    "shortCode": "github",
    "shortUrl": "http://localhost:8080/github"
  },
  {
    "id": 2,
    "originalUrl": "https://developer.android.com",
    "shortCode": "Ab3Lt9",
    "shortUrl": "http://localhost:8080/Ab3Lt9"
  }
]
```

## Project Structure

```text
src/main/kotlin/com/gblrod/encurtaai
│
├── config
├── controller
├── dto
├── entity
├── exception
├── extension
├── repository
├── service
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
