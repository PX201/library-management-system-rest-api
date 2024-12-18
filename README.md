# Library Management System API

The Library Management System API provides functionality for managing books, borrowers, transactions, and librarians in a library.

## Table of Contents

- [Introduction](#introduction)
- [Frontend Repository](#frontend-repository)
- [Setup](#setup)
- [Features](#features)
- [Endpoints](#endpoints)
- [Dependencies](#dependencies)
- [Getting Started](#getting-started)
- [Building and Running](#building-and-Running)
- [License](#license)

## Introduction

This API serves as the backend for a library management system, facilitating various operations such as book management, borrower management, transaction handling, and librarian administration.

## Frontend Repository

The frontend for this application is implemented using React.js, providing an intuitive and user-friendly interface for managing the library system.  
You can find the frontend repository here: [Library Management System - Frontend](https://github.com/your-username/library-management-frontend)


## Setup

### Prerequisites

- Java 17
- Maven
- MySQL

### Configuration

- **JWT Key**: A secret key used for JWT token generation and validation is stored in the `application.properties` file.
- **Default Admin Credentials**: Default administrator credentials are set in the `application.properties` file. Default admin email: `admin@admin.com`, password: `Admin2721`.
- **Database Configuration**: Configuration for the local database is set in the `application.properties` file (`librarydb` on MySQL).

## Features

- **Book Management:** Add, update, delete, and retrieve books. Track book details, availability, and borrowing history.
- **Borrower Management:** Register borrowers, manage their details and track their borrowing activity.
- **Transaction Handling:** Record borrowing and return transactions, manage late fees and handle damage fines.
- **Librarian Administration:** Administer librarian accounts, roles, and permissions.

## Endpoints

The API exposes the following endpoints:

- **`/api/v1/books`**: Endpoints related to book management.
- **`/api/v1/borrowers`**: Endpoints for borrower management.
- **`/api/v1/transactions`**: Endpoints for handling transactions.
- **`/api/v1/librarians`**: Endpoints for librarian administration.
- **`/api/v1/auth/login`**: Authentication endpoint to obtain a JWT token.
- **`/api/v1/test`**: Endpoint for testing authentication.

### Authentication

The API uses JWT (JSON Web Tokens) for authentication. To authenticate, make a POST request to `/api/v1/auth/login` with valid credentials (username and password) to obtain a JWT token. Include this token in the Authorization header for subsequent requests to access secured endpoints.

**`/api/v1/test`**: This endpoint requres valid authentication to access. Upon making a GET request with a valid JWT token in the authorization header, it responds with a `200 OK` status to indicate successful authentication.

## Dependencies

- Spring Boot Starter Data JPA
- Spring Boot Starter Web
- Spring Boot Starter security 
- JJWT for JWT token handling
- MySQL Connector
- Spring Boot Starter Test for testing purposes

## Getting Started

To run this project locally, follow these steps:

1. Clone the repository: `git clone <repository_url>`
2. Set up a MySQL database and configure the application properties accordingly.
3. Build the project using Maven or your preferred build tool.
4. Run the application.

## Building and Running

1. Clone this repository.
2. Configure your MySQL database and update the `application.properties` file accordingly.
3. Build the project using Maven: `mvn clean install`.
4. Run the application using Spring Boot: `mvn spring-boot:run`.


## License

This project is licensed under the [MIT License](LICENSE.md).

