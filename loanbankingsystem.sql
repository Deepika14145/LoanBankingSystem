-- Create Database
CREATE DATABASE IF NOT EXISTS Loan_System;

-- Use the database
USE Loan_System;

-- Table for Customer
CREATE TABLE IF NOT EXISTS Customer (
    CustomerID INT AUTO_INCREMENT PRIMARY KEY,
    Name VARCHAR(100) NOT NULL,
    Address VARCHAR(255) NOT NULL,
    Phone VARCHAR(15) NOT NULL,
    Email VARCHAR(100) NOT NULL
);

-- Table for Account
CREATE TABLE IF NOT EXISTS Account (
    AccountID INT AUTO_INCREMENT PRIMARY KEY,
    CustomerID INT,
    Balance DECIMAL(15, 2) NOT NULL,
    FOREIGN KEY (CustomerID) REFERENCES Customer(CustomerID) ON DELETE CASCADE
);

-- Table for Employees (Admin Login)
CREATE TABLE IF NOT EXISTS Employees (
    EmployeeID INT AUTO_INCREMENT PRIMARY KEY,
    Username VARCHAR(50) UNIQUE NOT NULL,
    Password VARCHAR(50) NOT NULL
);

-- Table for Loan Applications
CREATE TABLE IF NOT EXISTS LoanApplications (
    LoanID INT AUTO_INCREMENT PRIMARY KEY,
    AccountID INT,
    LoanAmount DECIMAL(15, 2) NOT NULL,
    Tenure INT NOT NULL, -- in months
    Status VARCHAR(50) DEFAULT 'Pending',
    FOREIGN KEY (AccountID) REFERENCES Account(AccountI
    D) ON DELETE CASCADE
);

-- Insert some sample data into Employees table for Admin Login
INSERT INTO Employees (Username, Password) VALUES
('admin', 'adminpassword'); -- You can change this later

-- Insert some sample data into Customer table for testing
INSERT INTO Customer (Name, Address, Phone, Email) VALUES
('John Doe', '123 Main St, City, State', '1234567890', 'johndoe@example.com'),
('Jane Smith', '456 Elm St, City, State', '0987654321', 'janesmith@example.com');
