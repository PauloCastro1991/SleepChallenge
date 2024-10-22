-- Create sequence for sleep log IDs
CREATE SEQUENCE sleep_log_id_seq START WITH 1 INCREMENT BY 1;

-- Create sleep_logs table with mood field
CREATE TABLE sleep_logs (
    id INT PRIMARY KEY DEFAULT nextval('sleep_log_id_seq'),
    user_id INT NOT NULL,
    sleep_start TIMESTAMP NOT NULL,
    sleep_end TIMESTAMP NOT NULL,
    mood VARCHAR(25),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

-- Insert sample sleep logs with mood
INSERT INTO sleep_logs (user_id, sleep_start, sleep_end, mood) VALUES (1, '2024-10-21 22:00:00', '2024-10-22 06:00:00', 'OK');
INSERT INTO sleep_logs (user_id, sleep_start, sleep_end, mood) VALUES (1, '2024-10-22 22:30:00', '2024-10-23 06:30:00', 'BAD');
INSERT INTO sleep_logs (user_id, sleep_start, sleep_end, mood) VALUES (2, '2024-10-21 23:00:00', '2024-10-22 07:00:00', 'GOOD');
