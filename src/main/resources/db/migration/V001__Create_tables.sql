CREATE SCHEMA IF NOT EXISTS cinematch;

-- Grant USAGE on the schema to postgres
GRANT USAGE ON SCHEMA cinematch TO postgres;

-- Set default privileges for future tables
ALTER DEFAULT PRIVILEGES IN SCHEMA cinematch GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO postgres;

CREATE TABLE cinematch.users
(
    id            SERIAL PRIMARY KEY,                     -- Unique user ID
    email         VARCHAR(255) UNIQUE NOT NULL,           -- User email (used for login)
    password_hash TEXT                NOT NULL,           -- Hashed password for security
    username      VARCHAR(50) UNIQUE  NOT NULL,           -- Display name
    created_at    TIMESTAMP WITH TIME ZONE DEFAULT NOW(), -- Account creation date
    updated_at    TIMESTAMP WITH TIME ZONE,               -- Last update timestamp
    last_login    TIMESTAMP WITH TIME ZONE                -- Last login timestamp
);


CREATE TABLE cinematch.user_sessions
(
    id            SERIAL PRIMARY KEY,                                                -- Unique session ID
    user_id       INT                      NOT NULL REFERENCES cinematch.users (id), -- Link to the user
    session_token TEXT UNIQUE              NOT NULL,                                 -- Token to identify the session
    created_at    TIMESTAMP WITH TIME ZONE DEFAULT NOW(),                            -- Session creation date
    expires_at    TIMESTAMP WITH TIME ZONE NOT NULL                                  -- Expiry for the session
);