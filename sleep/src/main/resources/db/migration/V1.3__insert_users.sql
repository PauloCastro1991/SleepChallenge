-- Insert sample data into the users table
DO $$
BEGIN
    -- Check if the 'users' table exists
    IF EXISTS (
        SELECT 1
        FROM information_schema.tables
        WHERE table_schema = 'public' AND table_name = 'users'
    ) THEN
        -- Insert sample users
        INSERT INTO users (username, email)
        VALUES
            ('john_doe', 'john.doe@example.com'),
            ('pcastro', 'pcastro@noom.com')
        ON CONFLICT (username) DO NOTHING;
    END IF;
END $$;