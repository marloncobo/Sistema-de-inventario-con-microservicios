INSERT INTO users (id, username, password_hash, role)
VALUES (
    gen_random_uuid(),
    'admin',
    '$2a$12$oedhgzXQo7g20Bl42lumw.uc1nUW3wiA3m646v6CUwiPNhROyKS7W',
    'ADMIN'
)
ON CONFLICT (username) DO UPDATE
SET password_hash = EXCLUDED.password_hash,
    role = EXCLUDED.role;
