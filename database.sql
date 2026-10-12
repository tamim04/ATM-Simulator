CREATE DATABASE IF NOT EXISTS atmsimulatorsystem;
USE atmsimulatorsystem;

-- Login credentials (one row per card)
CREATE TABLE IF NOT EXISTS login (
    formno VARCHAR(50),
    cardnumber VARCHAR(50) UNIQUE,
    pin VARCHAR(50)
);

-- Signup page 1: personal details
CREATE TABLE IF NOT EXISTS signup (
    formno VARCHAR(50),
    name VARCHAR(100),
    fname VARCHAR(100),
    dob VARCHAR(50),
    gender VARCHAR(20),
    email VARCHAR(100),
    marital VARCHAR(50),
    address VARCHAR(200),
    city VARCHAR(100),
    pincode VARCHAR(20),
    state VARCHAR(100)
);

-- Signup page 2: additional details
CREATE TABLE IF NOT EXISTS signuptwo (
    formno VARCHAR(50),
    religion VARCHAR(50),
    category VARCHAR(50),
    income VARCHAR(50),
    qualification VARCHAR(100),
    occupation VARCHAR(100),
    phone VARCHAR(50),
    nid VARCHAR(50),
    seniorcitizen VARCHAR(20),
    existingaccount VARCHAR(20)
);

-- Signup page 3: account details
CREATE TABLE IF NOT EXISTS signupThree (
    formno VARCHAR(50),
    accountType VARCHAR(50),
    cardnumber VARCHAR(50),
    pinnumber VARCHAR(50),
    facility VARCHAR(200)
);

-- Transactions, linked to the card number
CREATE TABLE IF NOT EXISTS bank (
    cardnumber VARCHAR(50),
    date VARCHAR(100),
    type VARCHAR(50),
    amount VARCHAR(50),
    INDEX (cardnumber)
);
