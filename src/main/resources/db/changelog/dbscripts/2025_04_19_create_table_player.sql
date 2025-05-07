CREATE TABLE players (
                         id BIGINT PRIMARY KEY,
                         username VARCHAR(100),
                         created_at TIMESTAMP NOT NULL DEFAULT NOW(),
                         last_active TIMESTAMP
);