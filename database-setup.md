# Setting Up MySQL Database for Library Management System API

This guide provides instructions on creating and connecting a MySQL database to the Library Management System REST API.

## Prerequisites

- MySQL Server installed locally or accessible through a remote server.
- MySQL Client (optional, recommended for direct interaction).

## Steps to Create and Connect the Database

### 1. Start MySQL Server

Ensure that your MySQL Server is up and running.

### 2. Access MySQL Shell

Log in to the MySQL shell using your terminal or preferred MySQL client.

```bash
mysql -u <username> -p
```


### 3. Create a New Database

In the MySQL shell, create a new database for the Library Management System:

```sql
CREATE DATABASE librarydb; -- Replace 'librarydb' with your preferred database name
USE librarydb;
```

### 4. Create Tables

Execute the following commands to create tables for the Library Management System:

#### Table: `genre`
```sql
-- Create the genre table
CREATE TABLE genre (
    genre_id INT NOT NULL AUTO_INCREMENT,
    genre_name VARCHAR(255),
    PRIMARY KEY (genre_id)
) ENGINE=InnoDB;
```

#### Table: `book
```sql
-- Create the book table
CREATE TABLE book (
    book_id BIGINT NOT NULL AUTO_INCREMENT,
    author VARCHAR(255),
    copies_number INT NOT NULL,
    description VARCHAR(255),
    is_all_signed_out BOOLEAN DEFAULT FALSE,
    isbn VARCHAR(255) UNIQUE,
    publication_year INT NOT NULL,
    shelf_address VARCHAR(255),
    signed_out_copies INT DEFAULT 0 NOT NULL,
    title VARCHAR(255),
    PRIMARY KEY (book_id)
) ENGINE=InnoDB;
```

#### Table: `book_genres` (for managing relationships)
```sql
-- Create the book_genres table for many-to-many relationship between book and genre
CREATE TABLE book_genres (
    book_book_id BIGINT NOT NULL,
    genres_genre_id INT NOT NULL,
    FOREIGN KEY (book_book_id) REFERENCES book(book_id),
    FOREIGN KEY (genres_genre_id) REFERENCES genre(genre_id)
) ENGINE=InnoDB;
```

#### Table: `borrower`
```sql
-- Create the borrower table
CREATE TABLE borrower (
    borrower_id BIGINT NOT NULL AUTO_INCREMENT,
    address VARCHAR(255),
    birth_date DATE,
    borrower_number VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    membership_status TINYINT,
    phone VARCHAR(255) UNIQUE,
    PRIMARY KEY (borrower_id)
) ENGINE=InnoDB;
```

#### Table: `transaction_record`
```sql
-- Create the transaction_record table
CREATE TABLE transaction_record (
    transaction_id BIGINT NOT NULL AUTO_INCREMENT,
    actual_return_date DATE,
    borrow_date DATE NOT NULL,
    damage_fine DOUBLE DEFAULT 0.00 CHECK (damage_fine >= 0),
    is_paid BIT DEFAULT 0,
    is_returned BIT DEFAULT 0,
    late_fee DOUBLE DEFAULT 0.00 CHECK (late_fee >= 0),
    return_date DATE NOT NULL,
    transaction_number VARCHAR(255) UNIQUE,
    book_id BIGINT,
    borrower_id BIGINT,
    PRIMARY KEY (transaction_id),
    FOREIGN KEY (book_id) REFERENCES book(book_id),
    FOREIGN KEY (borrower_id) REFERENCES borrower(borrower_id)
) ENGINE=InnoDB;
```

#### Table: `librarian`
```sql
-- Create the librarian table
CREATE TABLE librarian (
    id BIGINT NOT NULL,
    phone_number VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    password VARCHAR(255),
    pin SMALLINT NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB;
```

#### Table: `librarian_roles`
```sql
-- Create the librarian_roles table for managing roles of librarians
CREATE TABLE librarian_roles (
    librarian_id BIGINT NOT NULL,
    role ENUM ('ADMIN','LIBRARIAN','USER'),
    FOREIGN KEY (librarian_id) REFERENCES librarian(id)
) ENGINE=InnoDB;

-- Create a sequence table for librarian ID generation
CREATE TABLE librarian_seq (
    next_val BIGINT
) ENGINE=InnoDB;

-- Insert initial value into librarian_seq (if necessary)
INSERT INTO librarian_seq VALUES (1);
```

### 5. Connect to REST API

Configure the database connection details in your REST API's `application.properties` file:

```
spring.datasource.url=jdbc:mysql://localhost:3306/librarydb
spring.datasource.username=<your_mysql_username>
spring.datasource.password=<your_mysql_password>
```

Replace `<your_mysql_username>` and `<your_mysql_password>` with your MySQL credentials.

### 6. Verify Database Setup

- Check that the tables have been created by querying the database.
- Run your REST API and verify that it's connecting to the database without errors.
