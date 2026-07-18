-- NetBank Database Schema
-- Run this ONLY if spring.jpa.hibernate.ddl-auto=none
-- With ddl-auto=update, Hibernate auto-creates all tables.

CREATE DATABASE IF NOT EXISTS netbank_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE netbank_db;

-- Users table
CREATE TABLE IF NOT EXISTS users (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name   VARCHAR(100)        NOT NULL,
    email       VARCHAR(150)        NOT NULL UNIQUE,
    password    VARCHAR(255)        NOT NULL,
    phone       CHAR(10)            NOT NULL UNIQUE,
    role        ENUM('CUSTOMER','ADMIN') NOT NULL DEFAULT 'CUSTOMER',
    enabled     TINYINT(1)          NOT NULL DEFAULT 1,
    created_at  DATETIME            NOT NULL
);

-- Accounts table
CREATE TABLE IF NOT EXISTS accounts (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    account_number VARCHAR(16)  NOT NULL UNIQUE,
    account_type   ENUM('SAVINGS','CURRENT','SALARY') NOT NULL,
    balance        DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    status         ENUM('ACTIVE','INACTIVE','FROZEN','CLOSED') NOT NULL DEFAULT 'ACTIVE',
    created_at     DATETIME NOT NULL,
    user_id        BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Transactions table
CREATE TABLE IF NOT EXISTS transactions (
    id                     BIGINT AUTO_INCREMENT PRIMARY KEY,
    reference_number       VARCHAR(20)  NOT NULL UNIQUE,
    type                   ENUM('CREDIT','DEBIT','TRANSFER','BILL_PAYMENT') NOT NULL,
    amount                 DECIMAL(15,2) NOT NULL,
    balance_after          DECIMAL(15,2),
    description            VARCHAR(200),
    status                 ENUM('SUCCESS','FAILED','PENDING') NOT NULL DEFAULT 'SUCCESS',
    created_at             DATETIME NOT NULL,
    source_account_id      BIGINT,
    destination_account_id BIGINT,
    FOREIGN KEY (source_account_id)      REFERENCES accounts(id),
    FOREIGN KEY (destination_account_id) REFERENCES accounts(id),
    INDEX idx_txn_source (source_account_id),
    INDEX idx_txn_dest   (destination_account_id),
    INDEX idx_txn_date   (created_at)
);

-- Beneficiaries table
CREATE TABLE IF NOT EXISTS beneficiaries (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    name           VARCHAR(100) NOT NULL,
    account_number VARCHAR(16)  NOT NULL,
    bank_name      VARCHAR(100) NOT NULL,
    ifsc_code      VARCHAR(11),
    added_at       DATETIME NOT NULL,
    user_id        BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    UNIQUE KEY uq_user_account (user_id, account_number)
);

-- Bill Payments table
CREATE TABLE IF NOT EXISTS bill_payments (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    bill_type        ENUM('ELECTRICITY','WATER','GAS','INTERNET','MOBILE','DTH','INSURANCE','OTHER') NOT NULL,
    biller_name      VARCHAR(100) NOT NULL,
    consumer_number  VARCHAR(100) NOT NULL,
    amount           DECIMAL(12,2) NOT NULL,
    reference_number VARCHAR(20)  NOT NULL,
    status           ENUM('SUCCESS','FAILED','PENDING') NOT NULL DEFAULT 'SUCCESS',
    paid_at          DATETIME NOT NULL,
    account_id       BIGINT NOT NULL,
    FOREIGN KEY (account_id) REFERENCES accounts(id)
);

-- Seed admin user (password: Admin@1234)
INSERT IGNORE INTO users (full_name, email, password, phone, role, enabled, created_at)
VALUES ('Admin User', 'admin@netbank.com',
        '$2a$12$7J9Vw5z5uMq8rL6Xk3NpAeUhxYGkHJm4.Lq2OdKS.fR8TzR0IIOO',
        '9999999999', 'ADMIN', 1, NOW());
