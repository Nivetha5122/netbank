# NetBank — Internet Banking Web Application

A production-grade Spring Boot + JSP + Hibernate banking application built to demonstrate real-world Java web development skills.

## Tech Stack
| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.2, Spring MVC, Spring Security |
| ORM | Hibernate, Spring Data JPA |
| Web Layer | JSP, JSTL, Servlets |
| Database | MySQL 8 |
| Build Tool | Maven |
| Security | BCrypt (strength 12), Session-based auth, CSRF protection |

---

## Features

| Feature | Details |
|---|---|
| Registration & Login | Form-based auth, BCrypt password hashing, session timeout |
| Account Dashboard | Balance display, account type, status |
| Fund Transfer | Deadlock-safe pessimistic locking, 12+ REST endpoints |
| Mini Statement | Last 5 transactions |
| Full Statement | Paginated transaction history |
| Bill Payment | 8 categories (Electricity, Water, Mobile, etc.) |
| Beneficiary Management | Add / remove saved payees |
| REST API | 12+ JSON endpoints under `/api/v1/` |
| Role-based Access | CUSTOMER and ADMIN roles |
| Global Exception Handling | Custom exceptions per error type |

---

## Project Structure

```
netbank/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/com/netbank/
│   │   │   ├── NetBankApplication.java
│   │   │   ├── config/          SecurityConfig.java
│   │   │   ├── controller/      AuthController, DashboardController,
│   │   │   │                    TransferController, StatementController,
│   │   │   │                    BillPaymentController, BeneficiaryController,
│   │   │   │                    AccountApiController (REST)
│   │   │   ├── dto/             RegisterRequest, TransferRequest,
│   │   │   │                    BillPaymentRequest, ApiResponse
│   │   │   ├── entity/          User, Account, Transaction,
│   │   │   │                    Beneficiary, BillPayment
│   │   │   ├── exception/       GlobalExceptionHandler, NetBankException,
│   │   │   │                    InsufficientFundsException, AccountNotFoundException
│   │   │   ├── repository/      UserRepository, AccountRepository,
│   │   │   │                    TransactionRepository, BeneficiaryRepository,
│   │   │   │                    BillPaymentRepository
│   │   │   ├── service/         UserService, AccountService, TransactionService,
│   │   │   │   └── impl/        BillPaymentService, BeneficiaryService
│   │   │   └── util/            AccountUtils
│   │   ├── resources/
│   │   │   ├── application.properties
│   │   │   ├── schema.sql       (reference — Hibernate auto-creates tables)
│   │   │   └── static/
│   │   │       ├── css/main.css
│   │   │       └── js/main.js
│   │   └── webapp/WEB-INF/views/
│   │       ├── auth/            login.jsp, register.jsp
│   │       ├── dashboard/       home.jsp, navbar.jsp, sidebar.jsp,
│   │       │                    bills.jsp, beneficiaries.jsp
│   │       ├── transfer/        transfer.jsp, success.jsp
│   │       ├── statement/       statement.jsp
│   │       └── error/           error.jsp
│   └── test/java/com/netbank/
│       └── TransactionServiceTest.java
```

---

## Setup & Run

### Prerequisites
- Java 17+
- Maven 3.8+
- MySQL 8

### 1. Create Database
```sql
CREATE DATABASE netbank_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 2. Configure Credentials
Edit `src/main/resources/application.properties`:
```properties
spring.datasource.username=root
spring.datasource.password=your_password
```

### 3. Build & Run
```bash
mvn clean install
mvn spring-boot:run
```

### 4. Access
```
http://localhost:8080/netbank/auth/login
```

Register a new account or use the seeded admin (if schema.sql is run manually):
- Email: `admin@netbank.com`
- Password: `Admin@1234`

---

## REST API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/accounts` | List user accounts |
| GET | `/api/v1/accounts/{accountNumber}` | Account details |
| GET | `/api/v1/accounts/{accountNumber}/balance` | Account balance |
| POST | `/api/v1/transactions/transfer` | Fund transfer |
| GET | `/api/v1/transactions/mini?accountNumber=` | Mini statement |
| GET | `/api/v1/transactions/full?accountNumber=` | Full statement (paginated) |
| GET | `/api/v1/beneficiaries` | List beneficiaries |
| POST | `/api/v1/beneficiaries` | Add beneficiary |
| DELETE | `/api/v1/beneficiaries/{id}` | Remove beneficiary |
| POST | `/api/v1/bills/pay` | Pay bill |
| GET | `/api/v1/bills?accountNumber=` | Bill history |
| GET | `/api/v1/user/profile` | Current user profile |

---

## Key Design Decisions (Interview Points)

### Deadlock-safe Transfer
Two concurrent transfers between the same two accounts in opposite directions could deadlock. The `TransactionServiceImpl` locks accounts in **ascending account number order** using `@Lock(PESSIMISTIC_WRITE)`, eliminating this class of deadlock.

### Normalized Schema (3NF)
- `users` → `accounts` (one-to-many): user data is not duplicated on accounts
- `transactions` → references both source and destination accounts by FK
- `beneficiaries` has a unique constraint on `(user_id, account_number)`

### Role-based Access
Spring Security `@EnableMethodSecurity` + `hasRole("ADMIN")` on admin endpoints. Session management limits one active session per user.

### Custom Exception Hierarchy
`NetBankException` (base) → `InsufficientFundsException`, `AccountNotFoundException`. `GlobalExceptionHandler` returns JSON for API requests and JSP error page for browser requests by inspecting the `Accept` header.

---

## Run Tests
```bash
mvn test
```
