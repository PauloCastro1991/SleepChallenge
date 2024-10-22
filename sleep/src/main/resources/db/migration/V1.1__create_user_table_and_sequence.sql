-- Create sequence for user IDs
CREATE SEQUENCE user_id_seq START WITH 1 INCREMENT BY 1;

-- Create users table
CREATE TABLE users (
    id INT PRIMARY KEY DEFAULT nextval('user_id_seq'),
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert sample users
INSERT INTO users (username, email) VALUES ('john_doe', 'john.doe@example.com');
INSERT INTO users (username, email) VALUES ('pcastro', 'pcastro@noom.com');
