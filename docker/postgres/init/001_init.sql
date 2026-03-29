CREATE TABLE IF NOT EXISTS app_health_check (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO app_health_check (name)
VALUES ('postgres init success');