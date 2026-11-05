
# Employee Management REST API

A RESTful backend application developed using Java 21, Spring Boot 4, Spring Data JPA, and PostgreSQL.

This project demonstrates backend development, REST API design, database integration, dependency injection, validation, exception handling, and automated testing.

## Tech Stack

- Java 21
- Spring Boot 4.1.2
- Spring Data JPA and Hibernate
- PostgreSQL 18
- Maven
- Docker
- Swagger / OpenAPI
- JUnit 5 and Mockito

## Features

- Create new employees
- Retrieve all employees
- Retrieve employees by ID
- Update employee information
- Delete employees
- Validate employee details
- Prevent duplicate email addresses
- Handle HTTP 404 and 409 errors
- Interactive Swagger API documentation
- Automated unit tests

## Project Architecture

The application follows a layered architecture:

```text
Client / Swagger UI
        |
        v
EmployeeController
        |
        v
EmployeeService
        |
        v
EmployeeRepository
        |
        v
PostgreSQL Database
```

### Controller Layer

Handles incoming HTTP requests and returns REST API responses.

### Service Layer

Implements business logic, employee operations, and duplicate-email checks.

### Repository Layer

Uses Spring Data JPA for database operations.

### Model Layer

Defines the Employee entity and database mapping.

## REST API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/employees | Get all employees |
| GET | /api/employees/{id} | Get employee by ID |
| POST | /api/employees | Create employee |
| PUT | /api/employees/{id} | Update employee |
| DELETE | /api/employees/{id} | Delete employee |

## Example Request

POST /api/employees

```json
{
  "name": "Alex Johnson",
  "email": "alex.johnson@example.com",
  "department": "Software Engineering"
}
```

Successful response: HTTP 201 Created.

## Running Locally

### Prerequisites

- Java 21
- Docker
- Maven wrapper (included)

### Start PostgreSQL

```bash
docker run --name employee-postgres \
  -e POSTGRES_DB=employee_db \
  -e POSTGRES_USER=employee_user \
  -e POSTGRES_PASSWORD=employee_pass \
  -p 5434:5432 \
  -d postgres:18
```

If the container already exists:

```bash
docker start employee-postgres
```

### Configure Database Password

```bash
export DB_PASSWORD=employee_pass
```

### Start Spring Boot

```bash
./mvnw spring-boot:run
```

Application URL: http://localhost:8080

## Swagger Documentation

Access the interactive API documentation:

http://localhost:8080/swagger-ui/index.html

## Automated Testing

Run all tests:

```bash
./mvnw clean test
```

The project includes tests for employee retrieval, creation, duplicate email handling, missing employee errors, and application startup.

## HTTP Status Codes

| Status | Description |
|--------|-------------|
| 200 | Successful GET or PUT |
| 201 | Employee created |
| 204 | Employee deleted |
| 400 | Invalid request |
| 404 | Employee not found |
| 409 | Duplicate email |

## Author

Raaghul Nataraj Kannan

M.S. Data Science — Arizona State University

Java Backend Development | Spring Boot | REST APIs | PostgreSQL
