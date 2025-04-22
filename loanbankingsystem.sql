-- Create Database
CREATE DATABASE IF NOT EXISTS BankingLoanSystem;
USE BankingLoanSystem;

-- Table: Customer
CREATE TABLE IF NOT EXISTS Customer (
    CustomerID INT AUTO_INCREMENT PRIMARY KEY,
    Name VARCHAR(100) NOT NULL,
    Address VARCHAR(255) NOT NULL,
    Phone VARCHAR(15) NOT NULL,
    Email VARCHAR(100) NOT NULL
);

-- Table: Account
CREATE TABLE IF NOT EXISTS Account (
    AccountID INT AUTO_INCREMENT PRIMARY KEY,
    CustomerID INT,
    Balance DECIMAL(15, 2) NOT NULL,
    FOREIGN KEY (CustomerID) REFERENCES Customer(CustomerID) ON DELETE CASCADE
);

-- Table: Employees
CREATE TABLE IF NOT EXISTS Employees (
    EmployeeID INT AUTO_INCREMENT PRIMARY KEY,
    Username VARCHAR(50) UNIQUE NOT NULL,
    Password VARCHAR(50) NOT NULL
);

-- Table: Loan Applications
CREATE TABLE IF NOT EXISTS LoanApplications (
    LoanID INT AUTO_INCREMENT PRIMARY KEY,
    AccountID INT,
    LoanAmount DECIMAL(15, 2) NOT NULL,
    Tenure INT NOT NULL,
    Status VARCHAR(50) DEFAULT 'Pending',
    FOREIGN KEY (AccountID) REFERENCES Account(AccountID) ON DELETE CASCADE
);
ALTER TABLE LoanApplications
ADD COLUMN ApplicationDate DATE;

-- Table: Loan
CREATE TABLE IF NOT EXISTS Loan (
    LoanID INT AUTO_INCREMENT PRIMARY KEY,
    LoanAccountID INT,
    LoanAmount DECIMAL(15, 2) NOT NULL,
    InterestRate DECIMAL(5, 2),
    DurationMonths INT,
    EMIAmount DECIMAL(15, 2),
    StartDate DATE,
    FOREIGN KEY (LoanAccountID) REFERENCES Account(AccountID) ON DELETE CASCADE
);

-- Table: Complaint
CREATE TABLE IF NOT EXISTS Complaint (
    ComplaintID INT AUTO_INCREMENT PRIMARY KEY,
    AccountID INT NOT NULL,
    ComplaintText TEXT NOT NULL,
    ComplaintDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (AccountID) REFERENCES Account(AccountID) ON DELETE CASCADE
);

-- Table: Fixed Deposits
CREATE TABLE IF NOT EXISTS FixedDeposits (
    FDID INT AUTO_INCREMENT PRIMARY KEY,
    AccountID INT NOT NULL,
    DepositAmount DECIMAL(15, 2) NOT NULL,
    Tenure INT NOT NULL,
    FDStartDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (AccountID) REFERENCES Account(AccountID) ON DELETE CASCADE
);

ALTER TABLE FixedDeposits
ADD COLUMN PrincipalAmount DECIMAL(15, 2) NOT NULL;

ALTER TABLE FixedDeposits
ADD COLUMN InterestRate DECIMAL(5, 2) NOT NULL;

ALTER TABLE FixedDeposits
ADD COLUMN StartDate DATE;

-- Add the MaturityDate column
ALTER TABLE FixedDeposits
ADD COLUMN MaturityDate DATE;

-- Add the Status column
ALTER TABLE FixedDeposits
ADD COLUMN Status VARCHAR(20); -- Or ENUM('Active', 'Matured', 'Closed') if you prefer

-- Table: Loan EMI Calculations
CREATE TABLE IF NOT EXISTS LoanEMICalculations (
    CalculationID INT AUTO_INCREMENT PRIMARY KEY,
    PrincipalAmount DECIMAL(15, 2) NOT NULL,
    AnnualInterestRate DECIMAL(5, 2) NOT NULL,
    TenureMonths INT NOT NULL,
    EMIAmount DECIMAL(15, 2) NOT NULL,
    CalculationDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


-- Table: Loan Application Log
CREATE TABLE IF NOT EXISTS LoanApplicationLog (
    LogID INT AUTO_INCREMENT PRIMARY KEY,
    LoanID INT,
    AccountID INT,
    LoanAmount DECIMAL(15, 2)
);
drop table LoanApplicationLog;
-- Sample Data
INSERT INTO Employees (Username, Password) VALUES ('admin', 'admin123');

INSERT INTO Customer (Name, Address, Phone, Email) VALUES
('Ajay', '123 Main St, City, State', '1234567890', 'ajay@example.com'),
('Ram', '456 Elm St, City, State', '0987654321', 'ram@example.com');

INSERT INTO Account (CustomerID, Balance) VALUES
(1, 15000.00), (2, 25000.00);

INSERT INTO Loan (LoanAccountID, LoanAmount, InterestRate, DurationMonths, EMIAmount, StartDate)
VALUES (1, 50000.00, 8.5, 12, 4500.00, '2025-04-14');

INSERT INTO Complaint (AccountID, ComplaintText)
VALUES (1, 'ATM did not dispense cash but account debited.');

-- Views
CREATE OR REPLACE VIEW customerinfo AS
SELECT CustomerID, Name, Email FROM Customer WHERE CustomerID = 1;

CREATE OR REPLACE VIEW loaninfos AS 
SELECT AccountID, LoanAmount, Status FROM LoanApplications WHERE AccountID = 1;

-- Triggers
DELIMITER //
CREATE TRIGGER after_loan_application
AFTER INSERT ON LoanApplications
FOR EACH ROW
BEGIN
    INSERT INTO LoanApplicationLog (LoanID, AccountID, LoanAmount)
    VALUES (NEW.LoanID, NEW.AccountID, NEW.LoanAmount);
END;
//

CREATE TRIGGER lessamount
BEFORE INSERT ON Account 
FOR EACH ROW
BEGIN
    IF NEW.Balance < 100 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Initial deposit must be greater than 100';
    END IF;
END;
//

CREATE TRIGGER validate_phone
BEFORE INSERT ON Customer
FOR EACH ROW
BEGIN
    IF NEW.Phone NOT REGEXP '^[0-9]{10}$' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Invalid phone number. Must be 10 digits.';
    END IF;
END;
//

CREATE TRIGGER check_fd_amount
BEFORE INSERT ON FixedDeposits
FOR EACH ROW
BEGIN
    IF NEW.DepositAmount < 1000 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Fixed Deposit must be at least ₹1000';
    END IF;
END;
//
DELIMITER ;

-- Procedures
DELIMITER //

CREATE PROCEDURE GetCustomerInfo(IN cid INT)
BEGIN
    SELECT * FROM Customer WHERE CustomerID = cid;
END;

CREATE PROCEDURE GetLoanByAccount(IN accid INT)
BEGIN
    SELECT * FROM LoanApplications WHERE AccountID = accid;
END;

CREATE PROCEDURE UpdateBalance(IN accid INT, IN new_balance DECIMAL(15,2))
BEGIN
    UPDATE Account SET Balance = new_balance WHERE AccountID = accid;
END;

CREATE PROCEDURE GetCustomerFDs(IN cid INT)
BEGIN
    SELECT FD.* 
    FROM FixedDeposits FD
    JOIN Account A ON A.AccountID = FD.AccountID
    WHERE A.CustomerID = cid;
END;

CREATE PROCEDURE ShowAllCustomerNames()
BEGIN
    DECLARE done INT DEFAULT 0;
    DECLARE cname VARCHAR(100);
    DECLARE cur CURSOR FOR SELECT Name FROM Customer;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;

    OPEN cur;
    read_loop: LOOP
        FETCH cur INTO cname;
        IF done THEN LEAVE read_loop; END IF;
        SELECT CONCAT('Customer Name: ', cname);
    END LOOP;
    CLOSE cur;
END;

CREATE PROCEDURE ShowLowBalanceAccounts()
BEGIN
    DECLARE done INT DEFAULT 0;
    DECLARE accid INT;
    DECLARE bal DECIMAL(15,2);
    DECLARE cur CURSOR FOR SELECT AccountID, Balance FROM Account WHERE Balance < 5000;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;

    OPEN cur;
    read_loop: LOOP
        FETCH cur INTO accid, bal;
        IF done THEN LEAVE read_loop; END IF;
        SELECT CONCAT('Account ID: ', accid, ' has low balance of ₹', bal);
    END LOOP;
    CLOSE cur;
END;

CREATE PROCEDURE ShowAllComplaints()
BEGIN
    DECLARE done INT DEFAULT 0;
    DECLARE complaint TEXT;
    DECLARE cur CURSOR FOR SELECT ComplaintText FROM Complaint;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;

    OPEN cur;
    read_loop: LOOP
        FETCH cur INTO complaint;
        IF done THEN LEAVE read_loop; END IF;
        SELECT CONCAT('Complaint: ', complaint);
    END LOOP;
    CLOSE cur;
END;

//
DELIMITER ;
