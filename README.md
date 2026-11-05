# Employee Management REST API

A secure, Dockerized REST API built with Java 21, Spring Boot, Spring Security, JWT authentication, and PostgreSQL.

## Features

- Employee CRUD operations
- JWT authentication with registration and login
- Pagination and sorting
- Department filtering and employee search
- Input validation
- PostgreSQL persistence
- Docker Compose
- Automated tests and GitHub Actions CI

## Tech Stack

Java 21, Spring Boot, Spring Security, PostgreSQL, Maven, Docker, JUnit, Mockito.

## Getting Started

Requirements: Docker Desktop and Docker Compose.

Set the environment variables:

    export DB_PASSWORD=employee_pass
    export JWT_SECRET=$(openssl rand -base64 32)

Start the application:

    docker compose up -d --build

API URL: http://localhost:8081

## Authentication

Register: POST /api/auth/register

Login: POST /api/auth/login

Use the returned JWT token:

    Authorization: Bearer YOUR_TOKEN

## Employee Endpoints

- GET /api/employees
- POST /api/employees
- GET /api/employees/{id}
- PUT /api/employees/{id}
- DELETE /api/employees/{id}

Employee endpoints require JWT authentication.

## Testing

Run:

    ./mvnw clean test

GitHub Actions automatically runs tests on pushes and pull requests.

## Author

Raaghul Nataraj Kannan
