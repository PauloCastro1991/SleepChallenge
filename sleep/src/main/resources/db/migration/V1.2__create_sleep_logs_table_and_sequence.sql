CREATE SEQUENCE IF NOT EXISTS sleep_log_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE IF NOT EXISTS sleep_logs (
    id INT PRIMARY KEY DEFAULT nextval('sleep_log_id_seq'),
    user_id INT NOT NULL,
    sleep_start TIMESTAMP NOT NULL,
    sleep_end TIMESTAMP NOT NULL,
    mood VARCHAR(25) NOT NULL CHECK (mood IN ('BAD', 'GOOD', 'OK')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);