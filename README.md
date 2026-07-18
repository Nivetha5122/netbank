# NetBank - Secure Internet Banking Web Application

A full-stack Internet Banking application built using Spring Boot, Spring Security, Spring Data JPA, Hibernate, JSP, and MySQL. The project demonstrates secure authentication, banking operations, RESTful APIs, and enterprise Java development practices using a layered architecture.

---

## Overview

NetBank is designed to simulate a real-world banking system where users can securely manage their accounts, transfer funds, pay bills, manage beneficiaries, and view transaction history. The application follows industry-standard backend architecture and security practices.

---

## Technology Stack

| Layer | Technology |
|--------|------------|
| Language | Java 17 |
| Backend | Spring Boot 3.2, Spring MVC |
| Security | Spring Security, BCrypt Password Encoder |
| ORM | Hibernate, Spring Data JPA |
| Frontend | JSP, JSTL, HTML, CSS, JavaScript |
| Database | MySQL 8 |
| Build Tool | Maven |
| Server | Embedded Apache Tomcat |

---

## Features

- Secure User Registration and Login
- Password Encryption using BCrypt
- Role-Based Authentication and Authorization
- Account Dashboard
- Fund Transfer
- Mini Statement
- Transaction History
- Beneficiary Management
- Bill Payment
- REST API Support
- Session Management
- CSRF Protection
- Global Exception Handling

---

## Project Architecture

```
Client
   │
   ▼
Spring MVC Controllers
   │
   ▼
Service Layer
   │
   ▼
Repository Layer
   │
   ▼
Hibernate / Spring Data JPA
   │
   ▼
MySQL Database
```

---

## Project Structure

```
netbank/
│
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/com/netbank/
│   │   │   ├── config/
│   │   │   ├── controller/
│   │   │   ├── dto/
│   │   │   ├── entity/
│   │   │   ├── exception/
│   │   │   ├── repository/
│   │   │   ├── service/
│   │   │   ├── util/
│   │   │   └── NetBankApplication.java
│   │   │
│   │   ├── resources/
│   │   │   ├── application.properties
│   │   │   └── static/
│   │   │
│   │   └── webapp/
│   │       └── WEB-INF/views/
│   │
│   └── test/
│
└── README.md
```

---

## Database

Create the database before running the application.

```sql
CREATE DATABASE netbank_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

Update the database credentials in:

```
src/main/resources/application.properties
```

```properties
spring.datasource.username=root
spring.datasource.password=your_password
```

---

## Running the Application

### Prerequisites

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2-brightgreen)
![Spring Security](https://img.shields.io/badge/Spring_Security-6-green)
![MySQL](https://img.shields.io/badge/MySQL-8-blue)
![Hibernate](https://img.shields.io/badge/Hibernate-6-brown)
![Maven](https://img.shields.io/badge/Maven-Build-red)
![License](https://img.shields.io/badge/License-MIT-green)

### Build

```bash
mvn clean install
```

### Run

```bash
mvn spring-boot:run
```

---

## Application URL

```
http://localhost:8081/netbank/auth/login
```

---

## REST API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/accounts` | List user accounts |
| GET | `/api/v1/accounts/{accountNumber}` | Account details |
| GET | `/api/v1/accounts/{accountNumber}/balance` | Account balance |
| POST | `/api/v1/transactions/transfer` | Fund transfer |
| GET | `/api/v1/transactions/mini` | Mini statement |
| GET | `/api/v1/transactions/full` | Transaction history |
| GET | `/api/v1/beneficiaries` | List beneficiaries |
| POST | `/api/v1/beneficiaries` | Add beneficiary |
| DELETE | `/api/v1/beneficiaries/{id}` | Remove beneficiary |
| POST | `/api/v1/bills/pay` | Bill payment |
| GET | `/api/v1/bills` | Bill payment history |
| GET | `/api/v1/user/profile` | Current user profile |

---

## Security

- Spring Security Authentication
- BCrypt Password Encryption
- Session-Based Authentication
- CSRF Protection
- Role-Based Authorization
- Method-Level Security
- Password Hashing
- Secure Login and Logout

---

## Design Highlights

### Layered Architecture

The application follows a layered architecture consisting of:

- Controller Layer
- Service Layer
- Repository Layer
- Persistence Layer

This separation improves maintainability, scalability, and testability.

### Transaction Management

Fund transfer operations are designed to maintain transactional consistency using Spring's transaction management features.

### Database Design

- Normalized relational schema
- Entity relationships using JPA
- Foreign key constraints
- Repository pattern with Spring Data JPA

### Exception Handling

A centralized exception handling mechanism provides consistent error responses for both web pages and REST APIs.

---

## Testing

Run all unit tests using:

```bash
mvn test
```

---

## Future Enhancements

- JWT Authentication
- Email Verification
- Two-Factor Authentication
- Docker Deployment
- Redis Caching
- Notification Service
- Audit Logging
- API Documentation using Swagger/OpenAPI

---

## License

This project is intended for educational and portfolio purposes.

---

## Author

**Nivetha R**

MCA Student

Java | Spring Boot | Spring Security | Hibernate | MySQL
