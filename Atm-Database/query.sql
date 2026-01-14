CREATE DATABASE bankmanagementsystem;
USE bankmanagementsystem;

-- Create 1st Table with name signup
CREATE TABLE signup (
    formNo VARCHAR(30) PRIMARY KEY,
    name VARCHAR(30) NOT NULL,
    father_name VARCHAR(30) NOT NULL,
    dob VARCHAR(30) NOT NULL,
    gender VARCHAR(10) NOT NULL,
    email VARCHAR(50),  
    marital_status VARCHAR(20),
    address VARCHAR(100),
    city VARCHAR(30),
    state VARCHAR(30)
);

-- Table 2: Additional Info
CREATE TABLE signup2 (
    formNo VARCHAR(30),
    religion VARCHAR(30),
    nationality VARCHAR(30),
    income VARCHAR(30),
    education VARCHAR(30),
    occupation VARCHAR(30),
    CNIC VARCHAR(20),
    seniorCitizen VARCHAR(10),
    existingAccount VARCHAR(10),
    PRIMARY KEY (formNo),
    FOREIGN KEY (formNo) REFERENCES signup(formNo)
);

-- Table 3: Account Info
CREATE TABLE signup3 (
    formNo VARCHAR(30) PRIMARY KEY,
    accountType VARCHAR(40) NOT NULL,
    cardno VARCHAR(25) UNIQUE NOT NULL,
    pin VARCHAR(10) NOT NULL,
    FOREIGN KEY (formNo) REFERENCES signup(formNo)
);

CREATE TABLE account_services (
    id INT AUTO_INCREMENT PRIMARY KEY,
    formNo VARCHAR(30) NOT NULL,
    serviceName VARCHAR(50) NOT NULL,
    FOREIGN KEY (formNo) REFERENCES signup3(formNo)
);

-- Create the Login table to store login information
CREATE TABLE login (
    formno VARCHAR(20) NOT NULL,
    cardno VARCHAR(25),
    pin VARCHAR(10) NOT NULL,
    login_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (cardno),
    FOREIGN KEY (formno) REFERENCES signup(formno)
);

-- Create users table to store users 
CREATE TABLE users (
    cardno VARCHAR(25) PRIMARY KEY,       -- Unique identifier for each user
	pin VARCHAR(10) NOT NULL,
    currentBalance DECIMAL(10,2) DEFAULT 0.0      -- Current balance of user
);

-- Create transactions table to store transcations history
CREATE TABLE transactions (
    id INT AUTO_INCREMENT PRIMARY KEY,   
    cardno VARCHAR(25),                      -- Link to user
    transactionAmount DECIMAL(10,2),      -- Amount deposited/withdrawn
    type VARCHAR(20),                      
    datetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (cardno) REFERENCES users(cardno)
);

Select * from users;
Select * from login;
Select * from transactions;