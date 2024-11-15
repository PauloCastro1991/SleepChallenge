-- Create sequence for sleep log IDs if it does not exist
CREATE SEQUENCE IF NOT EXISTS sleep_log_id_seq START WITH 1 INCREMENT BY 1;

-- Create sleep_logs table if it does not exist
CREATE TABLE IF NOT EXISTS sleep_logs (
    id INT PRIMARY KEY DEFAULT nextval('sleep_log_id_seq'),
    user_id INT NOT NULL,
    sleep_start TIMESTAMP NOT NULL,
    sleep_end TIMESTAMP NOT NULL,
    mood VARCHAR(25),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);