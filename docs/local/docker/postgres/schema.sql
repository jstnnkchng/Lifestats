-- =========================
-- Users Table
-- =========================
CREATE TABLE IF NOT EXISTS users (
    user_id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(25),
    bio TEXT,
    join_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    );


-- =========================
-- Events Table
-- =========================
CREATE TABLE IF NOT EXISTS events (
    event_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    host_name VARCHAR(150) NOT NULL,
    event_name VARCHAR(255) NOT NULL,
    event_description TEXT,
    location VARCHAR(255),
    event_time TIMESTAMP NOT NULL,
    current_participants INTEGER NOT NULL DEFAULT 0,
    max_num_participants INTEGER NOT NULL,
    visibility VARCHAR(20) NOT NULL, -- e.g. PUBLIC, PRIVATE, FRIENDS_ONLY
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_events_user
    FOREIGN KEY (user_id)
    REFERENCES users(user_id)

    ON DELETE CASCADE,
    CONSTRAINT chk_participants_valid
    CHECK (current_participants >= 0
           AND max_num_participants > 0
           AND current_participants <= max_num_participants)
    );

-- =========================
-- Indexes (performance)
-- =========================
CREATE INDEX idx_events_user_id ON events(user_id);
CREATE INDEX idx_events_event_time ON events(event_time);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_username ON users(username);