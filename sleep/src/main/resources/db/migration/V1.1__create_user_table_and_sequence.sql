-- Create sequence for user IDs if it does not exist
CREATE SEQUENCE IF NOT EXISTS user_id_seq START WITH 1 INCREMENT BY 1;

-- Create users table if it does not exist
CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY DEFAULT nextval('user_id_seq'),
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);