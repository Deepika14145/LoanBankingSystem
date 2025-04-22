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
drop table Customer;

-- Table: Account
CREATE TABLE IF NOT EXISTS Account (
    AccountID INT AUTO_INCREMENT PRIMARY KEY,
    CustomerID INT,
    Balance DECIMAL(15, 2) NOT NULL,
    FOREIGN KEY (CustomerID) REFERENCES Customer(CustomerID) ON DELETE CASCADE
);
drop table Account;
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

-- Sample Data: Employees
INSERT INTO Employees (Username, Password) VALUES
('admin', 'admin123');

-- Sample Data: Customer
INSERT INTO Customer (Name, Address, Phone, Email) VALUES
('Ajay', '123 Main St, City, State', '1234567890', 'ajay@example.com'),
('Ram', '456 Elm St, City, State', '0987654321', 'ram@example.com');

-- Sample Data: Account
INSERT INTO Account (CustomerID, Balance) VALUES
(1, 15000.00), (2, 25000.00);

-- Table: Loan
DROP TABLE IF EXISTS Loan;
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

-- Sample Loan
INSERT INTO Loan (LoanAccountID, LoanAmount, InterestRate, DurationMonths, EMIAmount, StartDate)
VALUES (1, 50000.00, 8.5, 12, 4500.00, '2025-04-14');

-- Table: Complaint
CREATE TABLE IF NOT EXISTS Complaint (
    ComplaintID INT AUTO_INCREMENT PRIMARY KEY,
    AccountID INT NOT NULL,
    ComplaintText TEXT NOT NULL,
    ComplaintDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (AccountID) REFERENCES Account(AccountID) ON DELETE CASCADE
);

-- Sample Complaint
INSERT INTO Complaint (AccountID, ComplaintText)
VALUES (1, 'ATM did not dispense cash but account debited.');

-- Table: Fixed Deposits
CREATE TABLE IF NOT EXISTS FixedDeposits (
    FDID INT AUTO_INCREMENT PRIMARY KEY,
    AccountID INT NOT NULL,
    DepositAmount DECIMAL(15, 2) NOT NULL,
    Tenure INT NOT NULL,
    FDStartDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (AccountID) REFERENCES Account(AccountID) ON DELETE CASCADE
);

-- Table: Loan EMI Calculation
CREATE TABLE IF NOT EXISTS LoanEMICalculations (
    CalculationID INT AUTO_INCREMENT PRIMARY KEY,
    PrincipalAmount DECIMAL(15, 2) NOT NULL,
    AnnualInterestRate DECIMAL(5, 2) NOT NULL,
    TenureMonths INT NOT NULL,
    EMIAmount DECIMAL(15, 2) NOT NULL,
    CalculationDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- View: Customer Info
CREATE OR REPLACE VIEW customerinfo AS
SELECT CustomerID, Name, Email
FROM Customer
WHERE CustomerID = 1;

-- View: Loan Info
CREATE OR REPLACE VIEW loaninfos AS 
SELECT AccountID, LoanAmount, Status
FROM LoanApplications
WHERE AccountID = 1;

-- Join Query: Customer + Account
SELECT c.CustomerID, c.Name, a.Balance
FROM Customer c
JOIN Account a ON c.CustomerID = a.CustomerID;

-- Join Query: Complaints
SELECT a.CustomerID, c.ComplaintText, a.Balance
FROM Complaint c
JOIN Account a ON c.AccountID = a.AccountID;


CREATE TABLE IF NOT EXISTS LoanApplicationLog (
    LogID INT AUTO_INCREMENT PRIMARY KEY,
    LoanID INT,
    AccountID INT,
    LoanAmount DECIMAL(15, 2),
    ApplicationDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

DELIMITER //

CREATE TRIGGER after_loan_application
AFTER INSERT ON LoanApplications
FOR EACH ROW
BEGIN
    INSERT INTO LoanApplicationLog (LoanID, AccountID, LoanAmount)
    VALUES (NEW.LoanID, NEW.AccountID, NEW.LoanAmount);
END;
//

DELIMITER ;



-- Trigger: Prevent Low Initial Balance
DELIMITER //
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
DELIMITER ;

DELIMITER //

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

DELIMITER ;

DELIMITER //

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

-- procedures

-- Procedure: Get Customer Info by ID
DELIMITER //

CREATE PROCEDURE GetCustomerInfo(IN cid INT)
BEGIN
    SELECT * FROM Customer WHERE CustomerID = cid;
END;
//

DELIMITER ;

CALL GetCustomerInfo(1);

--  Procedure: Get Loan Details by Account ID
DELIMITER //

CREATE PROCEDURE GetLoanByAccount(IN accid INT)
BEGIN
    SELECT * FROM LoanApplications WHERE AccountID = accid;
END;
//

DELIMITER ;

CALL GetLoanByAccount(1);

-- Procedure: Update Account Balance

DELIMITER //

CREATE PROCEDURE UpdateBalance(IN accid INT, IN new_balance DECIMAL(15,2))
BEGIN
    UPDATE Account SET Balance = new_balance WHERE AccountID = accid;
END;
//

DELIMITER ;

CALL UpdateBalance(1, 12000.50);


DELIMITER //


-- Procedure: View All Fixed Deposits for a Customer

CREATE PROCEDURE GetCustomerFDs(IN cid INT)
BEGIN
    SELECT FD.* 
    FROM FixedDeposits FD
    JOIN Account A ON A.AccountID = FD.AccountID
    WHERE A.CustomerID = cid;
END;
//

DELIMITER ;


-- Cursor 1: Display All Customer Names (One by One)

DELIMITER //

CREATE PROCEDURE ShowAllCustomerNames()
BEGIN
    DECLARE done INT DEFAULT 0;
    DECLARE cname VARCHAR(100);
    
    DECLARE cur CURSOR FOR SELECT Name FROM Customer;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
    
    OPEN cur;
    
    read_loop: LOOP
        FETCH cur INTO cname;
        IF done THEN
            LEAVE read_loop;
        END IF;
        SELECT CONCAT('Customer Name: ', cname);
    END LOOP;
    
    CLOSE cur;
END;
//

DELIMITER ;

--  Cursor 2: Get Accounts with Balance Less Than 5000

DELIMITER //

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
        IF done THEN
            LEAVE read_loop;
        END IF;
        SELECT CONCAT('Account ID: ', accid, ' has low balance of ₹', bal);
    END LOOP;
    
    CLOSE cur;
END;
//

DELIMITER ;


-- Cursor 3: Show All Complaints (Text Only)

DELIMITER //

CREATE PROCEDURE ShowAllComplaints()
BEGIN
    DECLARE done INT DEFAULT 0;
    DECLARE complaint TEXT;
    
    DECLARE cur CURSOR FOR SELECT ComplaintText FROM Complaint;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
    
    OPEN cur;
    
    read_loop: LOOP
        FETCH cur INTO complaint;
        IF done THEN
            LEAVE read_loop;
        END IF;
        SELECT CONCAT('Complaint: ', complaint);
    END LOOP;
    
    CLOSE cur;
END;
//

DELIMITER ;
