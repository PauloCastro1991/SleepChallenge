DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM information_schema.tables
        WHERE table_schema = 'public' AND table_name = 'sleep_logs'
    ) THEN
        INSERT INTO sleep_logs (user_id, sleep_start, sleep_end, mood)
        VALUES
            (1, '2024-10-21 22:00:00', '2024-10-22 06:00:00', 'OK'),
            (1, '2024-10-22 22:30:00', '2024-10-23 06:30:00', 'BAD'),
            (2, '2024-10-21 23:00:00', '2024-10-22 07:00:00', 'GOOD');
    END IF;
END $$;