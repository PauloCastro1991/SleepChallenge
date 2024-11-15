DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM information_schema.tables
        WHERE table_schema = 'public' AND table_name = 'users'
    ) THEN
        INSERT INTO users (username, email)
        VALUES
            ('john_doe', 'john.doe@example.com'),
            ('pcastro', 'pcastro@noom.com')
        ON CONFLICT (username) DO NOTHING;
    END IF;
END $$;