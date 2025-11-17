-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role ENUM('admin', 'customer') NOT NULL
);
-- Create customers table
CREATE TABLE IF NOT EXISTS customers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    address VARCHAR(255) NOT NULL,
    meter_number VARCHAR(50) NOT NULL UNIQUE,
    user_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create bills table
CREATE TABLE IF NOT EXISTS bills (
    id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
    units_consumed INT NOT NULL,
    amount DOUBLE NOT NULL,
    bill_date DATE NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE
);

-- Insert sample admin
INSERT INTO users (username, password, role) VALUES ('admin', 'admin123', 'admin')
    ON DUPLICATE KEY UPDATE username=username;

-- Insert sample customer
INSERT INTO users (username, password, role) VALUES ('customer1', 'cust123', 'customer')
    ON DUPLICATE KEY UPDATE username=username;

-- Insert sample customer details (assuming user_id 2 for customer1)
INSERT INTO customers (name, address, meter_number, user_id) VALUES ('John Doe', '123 Main St', 'MTR1001', 2)
    ON DUPLICATE KEY UPDATE meter_number=meter_number;


