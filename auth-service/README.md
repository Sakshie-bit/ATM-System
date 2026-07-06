# Banking Management System (Spring Boot)

## Features

- Customer Registration
- Customer Login using JWT Authentication
- Create Bank Account
- Deposit Money
- Withdraw Money
- Fund Transfer
- Balance Inquiry
- Search Customer by Email
- Search Customer by Mobile Number
- View Transaction History
- Password Encryption using BCrypt
- MySQL Database Integration
- RESTful APIs

---

## Project Structure

```
auth-service
│
├── controller
│   ├── AuthController.java
│   └── AtmController.java
│
├── dto
│   ├── LoginRequest.java
│   ├── LoginResponse.java
│   ├── CreateAccountRequest.java
│   ├── AccountResponse.java
│   ├── CustomerResponse.java
│   └── TransferRequest.java
│
├── entity
│   ├── Customer.java
│   ├── BankAccount.java
│   └── Transaction.java
│
├── repository
│   ├── CustomerRepository.java
│   ├── BankAccountRepository.java
│   └── TransactionRepository.java
│
├── security
│   ├── JwtUtil.java
│   └── SecurityConfig.java
│
├── service
│   ├── CustomerService.java
│   └── impl
│       └── CustomerServiceImpl.java
│
├── resources
│   └── application.yml
│
└── AuthServiceApplication.java
```

---

## Database

Database Name:

```
banking_db
```

Tables:

- customers
- bank_accounts
- transactions

---

## API Endpoints

### Authentication APIs

| Method | Endpoint | Description |
|---------|----------|-------------|
| POST | /auth/register | Register Customer |
| POST | /auth/login | Login & Generate JWT Token |

---

### ATM APIs

| Method | Endpoint | Description |
|---------|----------|-------------|
| POST | /atm/account/create | Create Bank Account |
| GET | /atm/account/details | View Account Details |
| GET | /atm/balance | Check Balance |
| POST | /atm/deposit | Deposit Money |
| POST | /atm/withdraw | Withdraw Money |
| POST | /atm/transfer | Transfer Funds |
| GET | /atm/search/email | Search Customer by Email |
| GET | /atm/search/mobile | Search Customer by Mobile |
| GET | /atm/transactions | View Transaction History |

---

## Author

**Sakshi Potphode**

GitHub: https://github.com/Sakshie-bit