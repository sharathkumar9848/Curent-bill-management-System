# Current Bill Management System
A console-based Java application for managing electricity bills using JDBC and MySQL.
## Features
- User authentication (login/registration) with roles: admin, customer
- Admin: Add/Edit/Delete customers, generate bills, view all bills
- Customer: View profile, view current bill
- JDBC-based MySQL connectivity
## Project Structure
```
model/      # Java model classes (User, Customer, Bill)
dao/        # Data Access Objects for JDBC operations
service/    # Business logic (auth, billing, etc.)
db/         # Database schema and sample data
DBConnection.java  # MySQL connection utility
Main.java          # Main console UI
```
## Setup Instructions

### 1. MySQL Database
- Install MySQL and create a database named `current_bill_db`:
  ```sql
  CREATE DATABASE current_bill_db;
  ```
- Run the schema and sample data:
  ```sql
  USE current_bill_db;
  SOURCE db/schema.sql;
  ```

### 2. Configure JDBC
- Download the MySQL JDBC driver (Connector/J) from [MySQL Downloads](https://dev.mysql.com/downloads/connector/j/).
- Add the JDBC driver JAR to your classpath when compiling and running the project.

### 3. Update DB Credentials
- Edit `DBConnection.java` and set your MySQL username and password:
  ```java
  private static final String USER = "root";
  private static final String PASSWORD = "your_password";
  ```
### 4. Compile and Run
- Compile all Java files:
  ```sh
  javac -cp .;path/to/mysql-connector-java.jar model/*.java dao/*.java 
  java -cp ".;lib/*" Main
  
  ```
- Run the application:s
  ```shJ
  java -cp .;path/to/mysql-connector-java.jar Main
  ```

## Example Credentials
- Admin: `admin` / `admin123`
- Customer: `customer1` / `cust123`

---
No frameworks required. Java 17+ and MySQL only.



