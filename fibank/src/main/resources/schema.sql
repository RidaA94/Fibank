-- Drop tables if they exist to avoid conflicts during re-creation
DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS cashiers;

CREATE TABLE IF NOT EXISTS cashiers (
    id int AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    balanceBGN DOUBLE NOT NULL,
    balanceEUR DOUBLE NOT NULL,
    denominationsBGN VARCHAR(255) NOT NULL,
    denominationsEUR VARCHAR(255) NOT NULL
);

-- Create Transaction table
CREATE TABLE IF NOT EXISTS transactions (
    id int AUTO_INCREMENT PRIMARY KEY,
    cashier_id int NOT NULL,
    type VARCHAR(50) NOT NULL, -- Transaction type (Deposit, Withdrawal)
    amount DOUBLE NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (cashier_id) REFERENCES cashiers(id)
);

-- Optionally, insert initial data
INSERT INTO cashiers (name, balanceBGN, balanceEUR, denominationsBGN, denominationsEUR) VALUES
    ('MARTINA', 1000, 2000, '50x10, 10x50', '100x10, 20x50'),
    ('PETER', 1000, 2000, '50x10, 10x50', '100x10, 20x50'),
    ('LINDA', 1000, 2000, '50x10, 10x50', '100x10, 20x50');