## 📘 Overview

This is a Java-based console application that simulates the operations of an Automated Teller Machine.  Users can login, signup, deposit, withdraw, check balance, change PIN, perform fast cash, and view mini statements. 

---

## ⚙️ Core Features

- **User Authentication**: Login & Signup system.  
- **Transactions**: Deposit, Withdraw, Fast Cash, Balance Enquiry.  
- **Security**: PIN Change with validation.  
- **Mini Statement**: Shows recent transactions after last login.    
- **Input Validation**: Ensures correct numeric and PIN input.  
- **Exception Handling**: Graceful error handling throughout the system.  

## 🗂️ File Structure

```bash
ATM-Simulation/
│
├── atmsimulation/
│   ├── AtmSimulation.java        # Main class
│   ├── BaseScreen.java           # Abstract screen class
│   ├── Login.java                # Handles login
│   ├── Signup.java               # Handles registration
│   ├── Transactions.java         # Transaction menu & flow
│   ├── TransactionOperation.java # Abstract class for operations
│   ├── Deposit.java              # Deposit operation
│   ├── Withdraw.java             # Withdraw operation
│   ├── FastCash.java             # Fast cash operation
│   ├── BalanceEnquiry.java       # Balance check
│   ├── MiniStatement.java        # Shows mini statement
│   ├── Pin.java                  # PIN change
│
└── README.md                     # Project description
```


## 🚀 How to Run
```bash
# Compile all Java files
javac *.java

# Run the main program
java AtmSimulation
```
